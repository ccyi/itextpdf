/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the �GNU LIBRARY GENERAL PUBLIC LICENSE�), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;

import org.xml.sax.Parser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserFactory;

import com.lowagie.text.DocListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;

/**
 * This class can be used to parse an XML file.
 */

public class XmlParser {
    
/** We use this parser to parse the document. */
    public static final String PARSER = "org.apache.xerces.parsers.SAXParser";
    
/** This is the instance of the parser. */
    protected Parser parser;
    
/**
 * Constructs an XmlParser.
 */
    
    public XmlParser() throws DocumentException {
        try {
            parser = ParserFactory.makeParser(PARSER);
        }
        catch(ClassNotFoundException cnfe) {
            throw new ExceptionConverter(cnfe);
        }
        catch(IllegalAccessException iae) {
            throw new ExceptionConverter(iae);
        }
        catch(InstantiationException ie) {
            throw new ExceptionConverter(ie);
        }
    }
    
/**
 * Parses a given file.
 */
    
    public void go(DocListener document, InputSource is) throws DocumentException {
        try {
            parser.setDocumentHandler(new SAXiTextHandler(document));
            parser.parse(is);
        }
        catch(SAXException se) {
                    throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
                    throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 */
    
    public void go(DocListener document, InputSource is, String tagmap) throws DocumentException {
        try {
            parser.setDocumentHandler(new SAXmyHandler(document, new TagMap(tagmap)));
            parser.parse(is);
        }
        catch(SAXException se) {
                    throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
                    throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 */
    
    public void go(DocListener document, InputSource is, HashMap tagmap) throws DocumentException {
        try {
            parser.setDocumentHandler(new SAXmyHandler(document, tagmap));
            parser.parse(is);
        }
        catch(SAXException se) {
                    throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
                    throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 */
    
    public void go(DocListener document, String file) throws DocumentException {
        try {
            parser.setDocumentHandler(new SAXiTextHandler(document));
            parser.parse(file);
        }
        catch(SAXException se) {
                    throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
                    throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 */
    
    public void go(DocListener document, String file, String tagmap) throws DocumentException {
        try {
            parser.setDocumentHandler(new SAXmyHandler(document, new TagMap(tagmap)));
            parser.parse(file);
        }
        catch(SAXException se) {
                    throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
                    throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 */
    
    public void go(DocListener document, String file, HashMap tagmap) throws DocumentException {
        try {
            parser.setDocumentHandler(new SAXmyHandler(document, tagmap));
            parser.parse(file);
        }
        catch(SAXException se) {
                    throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
                    throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
    
    public static void parse(DocListener document, InputSource is) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, is);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
    
    public static void parse(DocListener document, InputSource is, String tagmap) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, is, tagmap);
    }
    
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 */
    
    public static void parse(DocListener document, InputSource is, HashMap tagmap) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, is, tagmap);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
    
    public static void parse(DocListener document, String file) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, file);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
    
    public static void parse(DocListener document, String file, String tagmap) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, file, tagmap);
    }
    
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 */
    
    public static void parse(DocListener document, String file, HashMap tagmap) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, file, tagmap);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
    
    public static void parse(DocListener document, InputStream is) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is));
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
    
    public static void parse(DocListener document, InputStream is, String tagmap) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is), tagmap);
    }
    
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 */
    
    public static void parse(DocListener document, InputStream is, HashMap tagmap) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is), tagmap);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
    
    public static void parse(DocListener document, Reader is) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is));
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
    
    public static void parse(DocListener document, Reader is, String tagmap) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is), tagmap);
    }
    
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 */
    
    public static void parse(DocListener document, Reader is, HashMap tagmap) throws DocumentException {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is), tagmap);
    }
}