package com.romanboehm.jsonwheel;

import com.fasterxml.jackson.jr.ob.JSON;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ComparisonTest {

    // Examples taken from https://json.org/example.html.
    @ParameterizedTest(name = ParameterizedTest.INDEX_PLACEHOLDER)
    @ValueSource(strings = {
            """
                    {
                        "glossary": {
                            "title": "example glossary",
                    		"GlossDiv": {
                                "title": "S",
                    			"GlossList": {
                                    "GlossEntry": {
                                        "ID": "SGML",
                    					"SortAs": "SGML",
                    					"GlossTerm": "Standard Generalized Markup Language",
                    					"Acronym": "SGML",
                    					"Abbrev": "ISO 8879:1986",
                    					"GlossDef": {
                                            "para": "A meta-markup language, used to create markup languages such as DocBook.",
                    						"GlossSeeAlso": ["GML", "XML"]
                                        },
                    					"GlossSee": "markup"
                                    }
                                }
                            }
                        }
                    }""",
            """
                    {"menu": {
                      "id": "file",
                      "value": "File",
                      "popup": {
                        "menuitem": [
                          {"value": "New", "onclick": "CreateNewDoc()"},
                          {"value": "Open", "onclick": "OpenDoc()"},
                          {"value": "Close", "onclick": "CloseDoc()"}
                        ]
                      }
                    }}""",
            """
                    {"widget": {
                        "debug": "on",
                        "window": {
                            "title": "Sample Konfabulator Widget",
                            "name": "main_window",
                            "width": 500,
                            "height": 500
                        },
                        "image": {
                            "src": "Images/Sun.png",
                            "name": "sun1",
                            "hOffset": 250,
                            "vOffset": 250,
                            "alignment": "center"
                        },
                        "text": {
                            "data": "Click Here",
                            "size": 36,
                            "style": "bold",
                            "name": "text1",
                            "hOffset": 250,
                            "vOffset": 100,
                            "alignment": "center",
                            "onMouseUp": "sun1.opacity = (sun1.opacity / 100) * 90;"
                        }
                    }}""",
            """
                    {"web-app": {
                      "servlet": [
                        {
                          "servlet-name": "cofaxCDS",
                          "servlet-class": "org.cofax.cds.CDSServlet",
                          "init-param": {
                            "configGlossary:installationAt": "Philadelphia, PA",
                            "configGlossary:adminEmail": "ksm@pobox.com",
                            "configGlossary:poweredBy": "Cofax",
                            "configGlossary:poweredByIcon": "/images/cofax.gif",
                            "configGlossary:staticPath": "/content/static",
                            "templateProcessorClass": "org.cofax.WysiwygTemplate",
                            "templateLoaderClass": "org.cofax.FilesTemplateLoader",
                            "templatePath": "templates",
                            "templateOverridePath": "",
                            "defaultListTemplate": "listTemplate.htm",
                            "defaultFileTemplate": "articleTemplate.htm",
                            "useJSP": false,
                            "jspListTemplate": "listTemplate.jsp",
                            "jspFileTemplate": "articleTemplate.jsp",
                            "cachePackageTagsTrack": 200,
                            "cachePackageTagsStore": 200,
                            "cachePackageTagsRefresh": 60,
                            "cacheTemplatesTrack": 100,
                            "cacheTemplatesStore": 50,
                            "cacheTemplatesRefresh": 15,
                            "cachePagesTrack": 200,
                            "cachePagesStore": 100,
                            "cachePagesRefresh": 10,
                            "cachePagesDirtyRead": 10,
                            "searchEngineListTemplate": "forSearchEnginesList.htm",
                            "searchEngineFileTemplate": "forSearchEngines.htm",
                            "searchEngineRobotsDb": "WEB-INF/robots.db",
                            "useDataStore": true,
                            "dataStoreClass": "org.cofax.SqlDataStore",
                            "redirectionClass": "org.cofax.SqlRedirection",
                            "dataStoreName": "cofax",
                            "dataStoreDriver": "com.microsoft.jdbc.sqlserver.SQLServerDriver",
                            "dataStoreUrl": "jdbc:microsoft:sqlserver://LOCALHOST:1433;DatabaseName=goon",
                            "dataStoreUser": "sa",
                            "dataStorePassword": "dataStoreTestQuery",
                            "dataStoreTestQuery": "SET NOCOUNT ON;select test='test';",
                            "dataStoreLogFile": "/usr/local/tomcat/logs/datastore.log",
                            "dataStoreInitConns": 10,
                            "dataStoreMaxConns": 100,
                            "dataStoreConnUsageLimit": 100,
                            "dataStoreLogLevel": "debug",
                            "maxUrlLength": 500}},
                        {
                          "servlet-name": "cofaxEmail",
                          "servlet-class": "org.cofax.cds.EmailServlet",
                          "init-param": {
                          "mailHost": "mail1",
                          "mailHostOverride": "mail2"}},
                        {
                          "servlet-name": "cofaxAdmin",
                          "servlet-class": "org.cofax.cds.AdminServlet"},
                        {
                          "servlet-name": "fileServlet",
                          "servlet-class": "org.cofax.cds.FileServlet"},
                        {
                          "servlet-name": "cofaxTools",
                          "servlet-class": "org.cofax.cms.CofaxToolsServlet",
                          "init-param": {
                            "templatePath": "toolstemplates/",
                            "log": 1,
                            "logLocation": "/usr/local/tomcat/logs/CofaxTools.log",
                            "logMaxSize": "",
                            "dataLog": 1,
                            "dataLogLocation": "/usr/local/tomcat/logs/dataLog.log",
                            "dataLogMaxSize": "",
                            "removePageCache": "/content/admin/remove?cache=pages&id=",
                            "removeTemplateCache": "/content/admin/remove?cache=templates&id=",
                            "fileTransferFolder": "/usr/local/tomcat/webapps/content/fileTransferFolder",
                            "lookInContext": 1,
                            "adminGroupID": 4,
                            "betaServer": true}}],
                      "servlet-mapping": {
                        "cofaxCDS": "/",
                        "cofaxEmail": "/cofaxutil/aemail/*",
                        "cofaxAdmin": "/admin/*",
                        "fileServlet": "/static/*",
                        "cofaxTools": "/tools/*"},
                      "taglib": {
                        "taglib-uri": "cofax.tld",
                        "taglib-location": "/WEB-INF/tlds/cofax.tld"}}}""",
            """
                    {"menu": {
                        "header": "SVG Viewer",
                        "items": [
                            {"id": "Open"},
                            {"id": "OpenNew", "label": "Open New"},
                            null,
                            {"id": "ZoomIn", "label": "Zoom In"},
                            {"id": "ZoomOut", "label": "Zoom Out"},
                            {"id": "OriginalView", "label": "Original View"},
                            null,
                            {"id": "Quality"},
                            {"id": "Pause"},
                            {"id": "Mute"},
                            null,
                            {"id": "Find", "label": "Find..."},
                            {"id": "FindAgain", "label": "Find Again"},
                            {"id": "Copy"},
                            {"id": "CopyAgain", "label": "Copy Again"},
                            {"id": "CopySVG", "label": "Copy SVG"},
                            {"id": "ViewSVG", "label": "View SVG"},
                            {"id": "ViewSource", "label": "View Source"},
                            {"id": "SaveAs", "label": "Save As"},
                            null,
                            {"id": "Help"},
                            {"id": "About", "label": "About Adobe CVG Viewer..."}
                        ]
                    }}"""
    })
    void produceSameMap(String json) throws IOException {
        var wheel = JsonWheel.read(json).inner;
        var jackson = JSON.std.mapFrom(json);
        assertThat(wheel).isEqualTo(jackson);
    }
}