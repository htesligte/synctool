import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class SyncTool
{
    public static Scanner scanner;
    public static Document document;
    public static NodeList sites;
    public static void main( String[] args )
    {
        try {
            System.out.println( "1. Add website" );
            System.out.println( "2. Download database" );
            System.out.println( "3. Download files" );
            scanner = new Scanner( System.in );
            int option = scanner.nextInt();
            switch( option )
            {
                case 1:
                    addWebsite();
                    break;
                case 2:
                    downloadDatabase();
                    break;
                case 3:
                    downloadFiles();
                    break;
            }
        } catch( Exception e ) {
            e.printStackTrace();
        }

    }

    public static int selectSite()
    {
        try {
            File fXmlFile = new File( "/home/henk/web/passwords/passwords.xml" );
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fXmlFile);
            document.getDocumentElement().normalize();

            sites = document.getElementsByTagName( "site" );

            for( int i = 0; i < sites.getLength(); i++ )
            {
                if( sites.item(i).getNodeType() == Node.ELEMENT_NODE )
                {
                    Element eElement = (Element) sites.item(i);
                    System.out.println( Integer.toString( i+1 ) + ". " + eElement.getElementsByTagName("name").item(0).getTextContent() );
                }
            }
            return scanner.nextInt();

        } catch( Exception e ) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void downloadFiles()
    {
        try {
            int siteId = selectSite();
            Element site = (Element) sites.item(siteId);

            String sitename = site.getElementsByTagName("name").item(0).getTextContent();
            String newsite = sitename.substring( 0, sitename.indexOf( "." ) ) + ".hts";

            String msg = "Nieuwe site [" + newsite + "]: ";
            String userinput = scanner.nextLine();
            if( userinput.length() > 0 )
                newsite = userinput;

            String directory = site.getElementsByTagName("files").item(0).getTextContent();


        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void downloadDatabase()
    {
        showSites();
    }

    public static void addWebsite()
    {
        showSites();
    }

    public static void ftpMirror( File dirname )
    {

    }
}
