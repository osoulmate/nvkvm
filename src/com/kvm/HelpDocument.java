package com.kvm;
import com.library.LoggerUtil;
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
        url = HelpDocument.class.getResource(path);
      }
      catch (Exception e) {
        LoggerUtil.error("Failed to open " + path);
        url = null;
      } 
      if (url != null)
      {
        this.html = new JEditorPane(url);
        this.html.setEditable(false);
        this.html.addHyperlinkListener(createHyperLinkListener());
        this.scroller = new JScrollPane(this.html);
      }
    } catch (MalformedURLException e) {
      LoggerUtil.error("Malformed URL: " + e.getClass().getName());
    }
    catch (IOException e) {
      LoggerUtil.error("IOException: " + e.getClass().getName());
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
                LoggerUtil.error("IOE: " + ioe.getClass().getName());
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
