/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package picotowriters;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class HTMLWriter {

    private StyledDocument _sd;
    private ElementIterator _it;

    protected static final char NEWLINE = '\n';

    public HTMLWriter(StyledDocument doc) {
        _sd = doc;
        _it = new ElementIterator(doc.getDefaultRootElement());
    }

    public String getHTML() {
        return "<html>" + this.getBody() + "</html>";
    }

    protected String getBody() {
        _it.current();

        Element next = null;

        String body = "<body>";

        while ((next = _it.next()) != null) {
            if (this.isText(next)) {
                body += writeContent(next);
            } else if (next.getName().equals("component")) {
                body += getText(next);
            }
        }
        body += "</body>";

        return body;
    }

    /**
     * Returns true if the element is a text element.
     */
    protected boolean isText(Element elem) {
        return (elem.getName() == AbstractDocument.ContentElementName);
    }

    protected String writeContent(Element elem) {

        AttributeSet attr = elem.getAttributes();

        String startTags = this.getStartTag(attr);

        String content = startTags + this.getText(elem) + this.getEndTag(startTags);

        return content;
    }

    /**
     * Escribe el texto
     */
    protected String text(Element elem) {
        String contentStr = getText(elem);
        
        //Si es un salto de línea mete un <br/>
        if ((contentStr.length() > 0) && (contentStr.charAt(contentStr.length() - 1) == NEWLINE)) {
            contentStr = contentStr.substring(0, contentStr.length() - 1) + "<br/>";
        }
        if (contentStr.length() > 0) {
            return contentStr;
        }
        return contentStr;
    }

    protected String getText(Element elem) {
        try {
            return _sd.getText(elem.getStartOffset(), elem.getEndOffset() - elem.getStartOffset()).replaceAll(NEWLINE + "", "<br/>");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getEndTag(String startTags) {

        String[] startOrder = startTags.split("<");

        String tags = "";

        for (String s : startOrder) {
            tags = "</" + s + tags;
        }

        return tags;
    }

    private String getStartTag(AttributeSet attr) {

        String tag = "";

        if (StyleConstants.isBold(attr)) {
            tag += "<b>";
        }
        if (StyleConstants.isItalic(attr)) {
            tag += "<i>";
        }
        if (StyleConstants.isUnderline(attr)) {
            tag += "<u>";
        }

        return tag;
    }
}
