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

package geotrellis.raster.crop

import geotrellis.vector._
import geotrellis.raster._


/**
  * A class containing extension methods for cropping [[Raster]]s.
  */
abstract class RasterCropMethods[T <: CellGrid[Int]: (? => CropMethods[T])](self: Raster[T]) extends CropMethods[Raster[T]] {
  import Crop.Options

  /**
    * Given an Extent and some cropping options, produce a cropped
    * [[Raster]].
    */
  def crop(extent: Extent, options: Options): Raster[T] = {
    val gridBounds = self.rasterExtent.gridBoundsFor(extent, clamp = options.clamp)
    val croppedExtent = self.rasterExtent.extentFor(gridBounds, clamp = options.clamp)
    val croppedTile = self.tile.crop(gridBounds, options)
    Raster(croppedTile, croppedExtent)
  }

  /**
   * Given an Extent, produce a cropped [[Raster]].
   */
  def crop(extent: Extent): Raster[T] =
    crop(extent, Options.DEFAULT)

  /**
    * Given a [[GridBounds]] and some cropping options, produce a new [[Raster]].
    */
  def crop(gridBounds: GridBounds[Int], options: Options): Raster[T] = {
    val croppedExtent = self.rasterExtent.extentFor(gridBounds, clamp = options.clamp)
    Raster(self.tile.crop(gridBounds, options), croppedExtent)
  }
}
