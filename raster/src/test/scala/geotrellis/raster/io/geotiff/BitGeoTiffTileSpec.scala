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

package geotrellis.raster.io.geotiff

import geotrellis.raster._

import geotrellis.raster.testkit._

import org.scalatest._

class BitGeoTiffTileSpec extends FunSpec
    with Matchers
    with RasterMatchers
    with BeforeAndAfterAll
    with GeoTiffTestUtils 
    with TileBuilders {
  describe("BitGeoTiffTile") {
    it("should map same") {
      val tile = SinglebandGeoTiff(geoTiffPath("bilevel.tif")).tile

      val res = tile.map { z => z }

      assertEqual(res, tile)
    }

    it("should map inverse") {
      val tile = SinglebandGeoTiff(geoTiffPath("bilevel.tif")).tile

      val res = tile.map { z => z ^ 1 }.map { z => z ^ 1 }

      assertEqual(res, tile)
    }

    it("should map with index") {
      val tile = SinglebandGeoTiff(geoTiffPath("bilevel.tif")).tile

      val res = tile.map { (col, row, z) => z }

      assertEqual(res, tile)
    }

    it("should map with index double") {
      val tile = SinglebandGeoTiff(geoTiffPath("bilevel.tif")).tile

      val res = tile.mapDouble { (col, row, z) => z }

      assertEqual(res, tile)
    }

    it("should map with index - tiled") {
      val tile = SinglebandGeoTiff(geoTiffPath("bilevel_tiled.tif")).tile

      val res = tile.map { (col, row, z) => z }

      assertEqual(res, tile)
    }

    it("should map with index double - tiled") {
      val tile = SinglebandGeoTiff(geoTiffPath("bilevel_tiled.tif")).tile

      val res = tile.mapDouble { (col, row, z) => z }

      assertEqual(res, tile)
    }

    it("should convert tiled tiffs") {
      val tiff = SinglebandGeoTiff(geoTiffPath("bilevel.tif"))
      val tile = tiff.tile.toArrayTile()

      // check that it is possible to convert bit cellType to bit cellType
      val tiffTile = tile.toGeoTiffTile.convert(BitCellType)
      assertEqual(tiffTile.toArrayTile(), tile.toArrayTile)

      // check that it is possible to convert int cellType to bit cellType
      // and that bitCellType conversion is idempotent
      (0 to 5).foldLeft(tile.toGeoTiffTile.convert(IntCellType)) { case (acc, _) =>
        val tiffTileLocal = acc.convert(BitCellType)
        assertEqual(tiffTileLocal.toArrayTile(), tile.toArrayTile)
        tiffTileLocal
      }
    }
  }
}
