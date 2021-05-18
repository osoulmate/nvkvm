package com.kvmV1;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
public class HelpDocument
{
  private JEditorPane html = null;
  private JScrollPane scroller = null;
  public HelpDocument(String pathUrl) {
    try {
      URL url = null;
      String path = pathUrl;
      try {
        url = getClass().getResource(path);
      }
      catch (Exception e) {
        System.err.println("Failed to open " + path);
        url = null;
      } 
      if (url != null)
      {
        this.html = new JEditorPane(url);
        this.html.setEditable(false);
        this.html.addHyperlinkListener(createHyperLinkListener());
        this.scroller = new JScrollPane();
        this.scroller.getViewport().add(this.html);
      }
    } catch (MalformedURLException e) {
      System.out.println("Malformed URL: " + e);
    }
    catch (IOException e) {
      System.out.println("IOException: " + e);
    } 
  }
  public HyperlinkListener createHyperLinkListener() {
    return new HyperlinkListener()
      {
        public void hyperlinkUpdate(HyperlinkEvent e)
        {
          if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
          {
            if (e instanceof HTMLFrameHyperlinkEvent) {
              ((HTMLDocument)HelpDocument.this.html.getDocument()).processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent)e);
            } else {
              try {
                HelpDocument.this.html.setPage(e.getURL());
              }
              catch (IOException ioe) {
                System.out.println("IOE: " + ioe);
              } 
            } 
          }
        }
      };
  }
  public JScrollPane getScroller() {
    return this.scroller;
  }
  public void setScroller(JScrollPane scroller) {
    this.scroller = scroller;
  }
}
