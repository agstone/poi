/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.xssf.usermodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLRelation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.model.CalculationChain;
import org.apache.poi.xssf.model.CommentsTable;
import org.apache.poi.xssf.model.MapInfo;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.SingleXmlCells;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.model.ThemesTable;

/**
 *
 */
public final class XSSFRelation extends POIXMLRelation {

    private static final POILogger log = POILogFactory.getLogger(XSSFRelation.class);

    /**
     * A map to lookup POIXMLRelation by its relation type
     */
    protected static final Map<String, XSSFRelation> _table = new HashMap<String, XSSFRelation>();


    public static final XSSFRelation WORKBOOK = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/workbook",
        "/xl/workbook.xml",
        null
    );
    public static final XSSFRelation MACROS_WORKBOOK = new XSSFRelation(
        "application/vnd.ms-excel.sheet.macroEnabled.main+xml",
        PackageRelationshipTypes.CORE_DOCUMENT,
        "/xl/workbook.xml",
        null
    );
    public static final XSSFRelation TEMPLATE_WORKBOOK = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.template.main+xml",
        PackageRelationshipTypes.CORE_DOCUMENT,
        "/xl/workbook.xml",
        null
    );

    public static final XSSFRelation MACRO_TEMPLATE_WORKBOOK = new XSSFRelation(
        "application/vnd.ms-excel.template.macroEnabled.main+xml",
        PackageRelationshipTypes.CORE_DOCUMENT,
        "/xl/workbook.xml",
        null
    );

    public static final XSSFRelation MACRO_ADDIN_WORKBOOK = new XSSFRelation(
        "application/vnd.ms-excel.addin.macroEnabled.main+xml",
        PackageRelationshipTypes.CORE_DOCUMENT,
        "/xl/workbook.xml",
        null
    );

    public static final XSSFRelation WORKSHEET = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet",
        "/xl/worksheets/sheet#.xml",
        XSSFSheet.class
    );

    public static final XSSFRelation CHARTSHEET = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.chartsheet+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chartsheet",
        "/xl/chartsheets/sheet#.xml",
        XSSFChartSheet.class
    );

    public static final XSSFRelation SHARED_STRINGS = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings",
        "/xl/sharedStrings.xml",
        SharedStringsTable.class
    );

    public static final XSSFRelation STYLES = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml",
        PackageRelationshipTypes.STYLE_PART,
        "/xl/styles.xml",
        StylesTable.class
    );

    public static final XSSFRelation DRAWINGS = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.drawing+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/drawing",
        "/xl/drawings/drawing#.xml",
        XSSFDrawing.class
    );

    public static final XSSFRelation VML_DRAWINGS = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.vmlDrawing",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing",
        "/xl/drawings/vmlDrawing#.vml",
        XSSFVMLDrawing.class
    );

    public static final XSSFRelation CHART = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.drawingml.chart+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/chart",
        "/xl/charts/chart#.xml",
        XSSFChart.class
    );

    public static final XSSFRelation CUSTOM_XML_MAPPINGS = new XSSFRelation(
        "application/xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/xmlMaps",
        "/xl/xmlMaps.xml",
        MapInfo.class
    );

    public static final XSSFRelation SINGLE_XML_CELLS = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.tableSingleCells+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/tableSingleCells",
        "/xl/tables/tableSingleCells#.xml",
        SingleXmlCells.class
    );

    public static final XSSFRelation TABLE = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.table+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/table",
        "/xl/tables/table#.xml",
        XSSFTable.class
    );

    public static final XSSFRelation IMAGES = new XSSFRelation(
        null,
        PackageRelationshipTypes.IMAGE_PART,
        null,
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_EMF = new XSSFRelation(
        "image/x-emf",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.emf",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_WMF = new XSSFRelation(
        "image/x-wmf",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.wmf",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_PICT = new XSSFRelation(
        "image/pict",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.pict",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_JPEG = new XSSFRelation(
        "image/jpeg",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.jpeg",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_PNG = new XSSFRelation(
        "image/png",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.png",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_DIB = new XSSFRelation(
        "image/dib",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.dib",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_GIF = new XSSFRelation(
        "image/gif",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.gif",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_TIFF = new XSSFRelation(
        "image/tiff",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.tiff",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_EPS = new XSSFRelation(
        "image/x-eps",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.eps",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_BMP = new XSSFRelation(
        "image/x-ms-bmp",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.bmp",
        XSSFPictureData.class
    );

    public static final XSSFRelation IMAGE_WPG = new XSSFRelation(
        "image/x-wpg",
        PackageRelationshipTypes.IMAGE_PART,
        "/xl/media/image#.wpg",
        XSSFPictureData.class
    );

    public static final XSSFRelation SHEET_COMMENTS = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.comments+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/comments",
        "/xl/comments#.xml",
        CommentsTable.class
    );

    public static final XSSFRelation SHEET_HYPERLINKS = new XSSFRelation(
        null,
        PackageRelationshipTypes.HYPERLINK_PART,
        null,
        null
    );

    public static final XSSFRelation OLEEMBEDDINGS = new XSSFRelation(
        null,
        POIXMLDocument.OLE_OBJECT_REL_TYPE,
        null,
        null
    );

    public static final XSSFRelation PACKEMBEDDINGS = new XSSFRelation(
        null,
        POIXMLDocument.PACK_OBJECT_REL_TYPE,
        null,
        null
    );

    public static final XSSFRelation VBA_MACROS = new XSSFRelation(
        "application/vnd.ms-office.vbaProject",
        "http://schemas.microsoft.com/office/2006/relationships/vbaProject",
        "/xl/vbaProject.bin",
        null
    );

    public static final XSSFRelation ACTIVEX_CONTROLS = new XSSFRelation(
        "application/vnd.ms-office.activeX+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/control",
        "/xl/activeX/activeX#.xml",
        null
    );

    public static final XSSFRelation ACTIVEX_BINS = new XSSFRelation(
        "application/vnd.ms-office.activeX",
        "http://schemas.microsoft.com/office/2006/relationships/activeXControlBinary",
        "/xl/activeX/activeX#.bin",
        null
    );

    public static final XSSFRelation THEME = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.theme+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme",
        "/xl/theme/theme#.xml",
        ThemesTable.class
    );

    public static final XSSFRelation CALC_CHAIN = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.calcChain+xml",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/calcChain",
        "/xl/calcChain.xml",
        CalculationChain.class
    );

    public static final XSSFRelation PRINTER_SETTINGS = new XSSFRelation(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.printerSettings",
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/printerSettings",
        "/xl/printerSettings/printerSettings#.bin",
        null
    );


    private XSSFRelation(String type, String rel, String defaultName, Class<? extends POIXMLDocumentPart> cls) {
        super(type, rel, defaultName, cls);

        if(cls != null && !_table.containsKey(rel)) _table.put(rel, this);
    }

    /**
     *  Fetches the InputStream to read the contents, based
     *  of the specified core part, for which we are defined
     *  as a suitable relationship
     */
    public InputStream getContents(PackagePart corePart) throws IOException, InvalidFormatException {
        PackageRelationshipCollection prc =
            corePart.getRelationshipsByType(_relation);
        Iterator<PackageRelationship> it = prc.iterator();
        if(it.hasNext()) {
            PackageRelationship rel = it.next();
            PackagePartName relName = PackagingURIHelper.createPartName(rel.getTargetURI());
            PackagePart part = corePart.getPackage().getPart(relName);
            return part.getInputStream();
        }
        log.log(POILogger.WARN, "No part " + _defaultName + " found");
        return null;
    }

    /**
     * Get POIXMLRelation by relation type
     *
     * @param rel relation type, for example,
     *    <code>http://schemas.openxmlformats.org/officeDocument/2006/relationships/image</code>
     * @return registered POIXMLRelation or null if not found
     */
    public static XSSFRelation getInstance(String rel){
        return _table.get(rel);
    }
}
