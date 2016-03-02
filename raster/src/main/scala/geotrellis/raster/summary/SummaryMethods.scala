package geotrellis.raster.summary

import geotrellis.raster._
import geotrellis.raster.histogram._
import geotrellis.util.MethodExtensions


trait SummaryMethods extends MethodExtensions[Tile] {
  /**
    * Contains several different operations for building a histograms of a raster.
    *
    * @note     Tiles with a double type (FloatConstantNoDataCellType, DoubleConstantNoDataCellType) will have their values
    *           rounded to integers when making the Histogram.
    */
  def histogram: Histogram[Int] =
    FastMapHistogram.fromTile(self)

  /**
    * Implements a histogram in terms of an array of the given size.
    * The size provided must be less than or equal to the number of distinct
    * values in the Tile.
    *
    * @note     Tiles with a double type (FloatConstantNoDataCellType, DoubleConstantNoDataCellType) will have their values
    *           rounded to integers when making the Histogram.
    */
  def arrayHistogram(size: Int): ArrayHistogram =
    ArrayHistogram.fromTile(self, size)

  /**
    * Create a histogram from double values in a raster.
    *
    * FastMapHistogram only works with integer values, which is great for performance
    * but means that, in order to use FastMapHistogram with double values, each value
    * must be multiplied by a power of ten to preserve significant fractional digits.
    *
    * For example, if you want to save one significant digit (2.1 from 2.123), set
    * sigificantDigits to 1, and the histogram will save 2.1 as "21" by multiplying
    * each value by 10.  The multiplier is 10 raised to the power of significant digits
    * (e.g. 1 digit: 10, 2 digits: 100, and so on).
    *
    * Important: Be sure that the maximum value in the rater multiplied by
    *            10 ^ significantDigits does not overflow Int.MaxValue (2, 147, 483, 647).
    *
    * @param significantDigits   Number of significant digits to preserve by multiplying
    */
  def doubleHistogram(significantDigits: Int): Histogram[Int] =
    FastMapHistogram.fromTileDouble(self, significantDigits)

  /**
  * Generate quantile class breaks for a given raster.
  */
  def classBreaks(numBreaks: Int): Array[Int] =
    histogram.getQuantileBreaks(numBreaks)

  /**
    * Determine statistical data for the given histogram.
    *
    * This includes mean, median, mode, stddev, and min and max values.
    */
  def statistics: Statistics[Int] =
    histogram.generateStatistics

  /**
   * Calculate a raster in which each value is set to the standard deviation of that cell's value.
   *
   * @return        Tile of IntConstantNoDataCellType data
   *
   * @note          Currently only supports working with integer types. If you pass in a Tile
   *                with double type data (FloatConstantNoDataCellType, DoubleConstantNoDataCellType) the values will be rounded to
   *                Ints.
   */
  def standardDeviations(factor: Double = 1.0): Tile = {
    val Statistics(_, mean, _, _, stddev, _, _) = statistics

    val indata = self.toArray
    val len = indata.length
    val result = Array.ofDim[Int](len)

    var i = 0
    while (i < len) {
      val delta = indata(i) - mean
      result(i) = (delta * factor / stddev).toInt
      i += 1
    }

    ArrayTile(result, self.cols, self.rows)
  }
}