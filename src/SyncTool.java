import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.PrintCommandListener;

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
            String userinput;
            int siteId = selectSite();
            Element site = (Element) sites.item(siteId);

            String sitename = site.getElementsByTagName("name").item(0).getTextContent();
            String newsite = sitename.substring( 0, sitename.indexOf( "." ) ) + ".hts";

            String msg = "Nieuwe site [" + newsite + "]: ";
            userinput = scanner.nextLine();
            if( userinput.length() > 0 )
                newsite = userinput;

            String sLocalDir = "/home/henk/web/" + newsite;
            File LocalDir = getDownloadDir( new File( sLocalDir ) );


            String directory = site.getElementsByTagName("directory").item(0).getTextContent();

            String username = site.getElementsByTagName( "username" ).item(0).getTextContent();
            String password = site.getElementsByTagName( "password" ).item(0).getTextContent();

            FTPClient ftp = new FTPClient();
            ftp.connect( "94.126.71.123" );
            ftp.login( username, password );
            ftp.addProtocolCommandListener( new PrintCommandListener( new PrintWriter( System.out ) ) );
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(directory);

            File LocalDir = new File( "/home/henk/web/" + newsite );


        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    public static File getDownloadDir( File requestedDir )
    {
        try {
            if( !requestedDir.exists() )
                return requestedDir;

            String userinput;
            System.out.println( "Directory " + requestedDir.getName() + " bestaat al. (R)enamen of (V)erwijderen?" );
            userinput = scanner.nextLine();
            if( userinput.equalsIgnoreCase("r") )
            {
                DateFormat dateFormat = new SimpleDateFormat( "ddMMyyyy-HHmmss" );
                Date date = new Date();

                String newdirname = requestedDir.getName() + "-" + dateFormat.format(date);
                System.out.println( "Nieuwe dirname: [" + newdirname + "]" );
                userinput = scanner.nextLine();
                if( userinput.length() > 0 )
                    newdirname = userinput;

                requestedDir.renameTo( new File( "/home/henk/web/" + newdirname ))

                return getDownloadDir( new File( "/home/henk/web/" + newdirname ) );
            }
            else if( userinput.equalsIgnoreCase( "v" ) )
            {
                FileUtils.deleteDirectory(requestedDir);
                return requestedDir;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadDatabase()
    {
        showSites();
    }

    public static void addWebsite()
    {
        showSites();
    }

    public static void ftpMirror( File LocalDir, FTPClient ftp )
    {

    }
}
