/*
 * Copyright 2016 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geotrellis.doc.examples.raster

import geotrellis.raster._
import geotrellis.raster.reproject._
import geotrellis.raster.resample._
import geotrellis.spark.reproject._
import geotrellis.vector.Extent

object MatchingRasters {
  def `Matching two rasters of a different CRS so that you can perform operations between them. [1]` = {
    val raster1: ProjectedRaster[Tile] = ???
    val raster2: ProjectedRaster[Tile] = ???
    val areaOfInterest: Extent = ???

    // Weights for our weighted sum
    val (w1, w2): (Int, Int) = ???

    val cropped1 =
      raster1.raster.crop(areaOfInterest)

    val cropped2 =
      raster2
        .reproject(raster1.crs, DefaultTarget).raster
        .resample(TargetRegion(raster1.raster.rasterExtent))
        .crop(areaOfInterest)

    val result = cropped1.tile * w1 + cropped2.tile * w2
  }

  def `Matching two rasters of a different CRS so that you can perform operations between them. [2]` = {
    val raster1: ProjectedRaster[Tile] = ???
    val raster2: ProjectedRaster[Tile] = ???
    val areaOfInterest: Extent = ???

    // Weights for our weighted sum
    val (w1, w2): (Int, Int) = ???

    val cropped1 =
      raster1.raster.crop(areaOfInterest)

    val cropped2 =
      raster2
        .reproject(raster1.crs, TargetRegion(raster1.raster.rasterExtent))

    val result = cropped1.tile * w1 + cropped2.tile * w2
  }
}
