/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package got.utility;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConnectionCreate extends JFrame {
    private static final long serialVersionUID = -5633998810385136625L;
    private Image m_image; // The map image will be stored here
    private Map<String, Point> m_centers = new HashMap<String, Point>(); // hash
    // map
    // for
    // center
    // points
    private Map<String, List<Polygon>> m_polygons = new HashMap<String, List<Polygon>>(); // hash
    // map
    // for
    // polygon
    // points
    private final JLabel m_location = new JLabel();

    private List<Connection> m_conns = new ArrayList<Connection>();

    private Connection conn = null;

    private boolean drawline = false;

    class Connection {
        public String start;
        public String end;
        public Point sp;
        public Point ep;

        public Connection(String start, String end, Point sp, Point ep) {
            this.start = start;
            this.end = end;
            this.sp = sp;
            this.ep = ep;
        }

        public Connection() {
        }
    }

    /**
     * main(java.lang.String[])
     * 
     * Main program begins here. Asks the user to select the map then runs the
     * the actual picker.
     * 
     * @param java
     *            .lang.String[] args the command line arguments
     * @see Picker(java.lang.String) picker
     */
    public static void main(final String[] args) {
        System.out.println("Select the map");
        // final String mapName = new
        // FileOpen("Select The Map").getPathString();
        final String mapName = "/home/zane/workspace/Agot-Java/src/main/java/got/resource/agot.png";
        if (mapName != null) {
            System.out.println("Map : " + mapName);
            final ConnectionCreate picker = new ConnectionCreate(mapName);
            picker.setSize(1024, 768);
            picker.setVisible(true);
        } else {
            System.out.println("No Image Map Selected. Shutting down.");
            System.exit(0);
        }
    }// end main

    /**
     * Constructor CenterPicker(java.lang.String)
     * 
     * Setus up all GUI components, initializes variables with default or needed
     * values, and prepares the map for user commands.
     * 
     * @param java
     *            .lang.String mapName name of map file
     */
    public ConnectionCreate(final String mapName) {
        super("Center Picker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final File file = new File("/home/zane/workspace/Agot-Java/src/main/java/got/txt/polygons.txt");
        if (file.exists()
                && JOptionPane
                        .showConfirmDialog(
                                new JPanel(),
                                "A polygons.txt file was found in the map's folder, do you want to use the file to supply the territories names?",
                                "File Suggestion", 1) == 0) {
            try {
                System.out.println("Polygons : " + file.getPath());
                m_polygons = PointFileReaderWriter.readOneToManyPolygons(new FileInputStream(file.getPath()));
            } catch (final IOException ex1) {
                ex1.printStackTrace();
            }
        } else {
            try {
                System.out.println("Select the Polygons file");
                // final String polyPath = new
                // FileOpen("Select A Polygon File").getPathString();
                final String polyPath = "/home/zane/workspace/boardgame_client/src/got/resource/txt/polygons.txt.bak";
                if (polyPath != null) {
                    System.out.println("Polygons : " + polyPath);
                    m_polygons = PointFileReaderWriter.readOneToManyPolygons(new FileInputStream(polyPath));
                } else {
                    System.out.println("Polygons file not given. Will run regardless");
                }
            } catch (final IOException ex1) {
                ex1.printStackTrace();
            }
        }
        createImage(mapName);
        final JPanel imagePanel = createMainPanel();
        /*
         * Add a mouse listener to show X : Y coordinates on the lower left
         * corner of the screen.
         */
        imagePanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                m_location.setText("x:" + e.getX() + " y:" + e.getY());
            }
        });
        /*
         * Add a mouse listener to monitor for right mouse button being clicked.
         */
        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                mouseEvent(e.getPoint(), e.isControlDown(), SwingUtilities.isRightMouseButton(e));
            }
        });
        // set up the image panel size dimensions ...etc
        imagePanel.setMinimumSize(new Dimension(m_image.getWidth(this), m_image.getHeight(this)));
        imagePanel.setPreferredSize(new Dimension(m_image.getWidth(this), m_image.getHeight(this)));
        imagePanel.setMaximumSize(new Dimension(m_image.getWidth(this), m_image.getHeight(this)));
        // set up the layout manager
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(new JScrollPane(imagePanel), BorderLayout.CENTER);
        this.getContentPane().add(m_location, BorderLayout.SOUTH);
        // set up the actions
        final Action openAction = new AbstractAction("Load Centers") {
            private static final long serialVersionUID = 2712234474452114083L;

            public void actionPerformed(final ActionEvent event) {
                loadCenters();
            }
        };
        openAction.putValue(Action.SHORT_DESCRIPTION, "Load An Existing Center Points File");
        final Action saveAction = new AbstractAction("Save Centers") {
            private static final long serialVersionUID = -4519036149978621171L;

            public void actionPerformed(final ActionEvent event) {
                saveCenters();
            }
        };
        saveAction.putValue(Action.SHORT_DESCRIPTION, "Save The Center Points To File");
        final Action exitAction = new AbstractAction("Exit") {
            private static final long serialVersionUID = -5631457890653630218L;

            public void actionPerformed(final ActionEvent event) {
                System.exit(0);
            }
        };
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit The Program");
        // set up the menu items
        final JMenuItem openItem = new JMenuItem(openAction);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        final JMenuItem saveItem = new JMenuItem(saveAction);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        final JMenuItem exitItem = new JMenuItem(exitAction);
        // set up the menu bar
        final JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
    }// end constructor

    /**
     * createImage(java.lang.String)
     * 
     * creates the image map and makes sure it is properly loaded.
     * 
     * @param java
     *            .lang.String mapName the path of image map
     */
    private void createImage(final String mapName) {
        m_image = Toolkit.getDefaultToolkit().createImage(mapName);
        try {
            Util.ensureImageLoaded(m_image);
        } catch (final InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * javax.swing.JPanel createMainPanel()
     * 
     * Creates the main panel and returns a JPanel object.
     * 
     * @return javax.swing.JPanel the panel to return
     */
    private JPanel createMainPanel() {
        final JPanel imagePanel = new JPanel() {
            private static final long serialVersionUID = -7130828419508975924L;

            @Override
            public void paint(final Graphics g) {
                // super.paint(g);
                g.drawImage(m_image, 0, 0, this);
                g.setColor(Color.red);
                // for (final String centerName : m_centers.keySet()) {
                // final Point item = m_centers.get(centerName);
                // g.fillOval(item.x, item.y, 15, 15);
                // g.drawString(centerName, item.x + 17, item.y + 13);
                // }
                for (Connection tc : m_conns) {
                    g.drawLine(tc.sp.x, tc.sp.y, tc.ep.x, tc.ep.y);
                }
            }
        };
        return imagePanel;
    }

    /**
     * saveCenters()
     * 
     * Saves the centers to disk.
     */
    private void saveCenters() {
        try {
            final String fileName = new FileSave("Where To Save centers.txt ?", "connection.xml").getPathString();
            if (fileName == null) {
                return;
            }
            final FileOutputStream out = new FileOutputStream(fileName);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Connections");
            doc.appendChild(rootElement);

            for (Connection conn : m_conns) {
                Element connection = doc.createElement("Connection");
                rootElement.appendChild(connection);

                // set attribute to staff element
                Attr attr = doc.createAttribute("t1");
                attr.setValue(conn.start);
                connection.setAttributeNode(attr);
                Attr attr1 = doc.createAttribute("t2");
                attr1.setValue(conn.end);
                connection.setAttributeNode(attr1);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
     
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
     
            transformer.transform(source, result);
            System.out.println("Data written to :" + new File(fileName).getCanonicalPath());
        } catch (final FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (final HeadlessException ex) {
            ex.printStackTrace();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * loadCenters()
     * 
     * Loads a pre-defined file with map center points.
     */
    private void loadCenters() {
        try {
            System.out.println("Load a center file");
            final String centerName = new FileOpen("Load A Center File").getPathString();
            if (centerName == null) {
                return;
            }
            final FileInputStream in = new FileInputStream(centerName);
            m_centers = PointFileReaderWriter.readOneToOne(in);
            repaint();
        } catch (final FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (final IOException ex) {
            ex.printStackTrace();
        } catch (final HeadlessException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * java.lang.String findTerritoryName(java.awt.Point)
     * 
     * Finds a land territory name or some sea zone name.
     * 
     * @param java
     *            .awt.point p a point on the map
     */
    private String findTerritoryName(final Point p) {
        String seaName = "unknown";
        // try to find a land territory.
        // sea zones often surround a land territory
        for (final String name : m_polygons.keySet()) {
            final Collection<Polygon> polygons = m_polygons.get(name);
            for (final Polygon poly : polygons) {
                if (poly.contains(p)) {
                    if (name.endsWith("Sea Zone") || name.startsWith("Sea Zone")) {
                        seaName = name;
                    } else {
                        return name;
                    }
                }// if
            }// while
        }// while
        return seaName;
    }

    /**
     * mouseEvent(java.awt.Point, java.lang.boolean, java.lang.boolean)
     * 
     * @param java
     *            .awt.Point point a point clicked by mouse
     * @param java
     *            .lang.boolean ctrlDown true if ctrl key was hit
     * @param java
     *            .lang.boolean rightMouse true if the right mouse button was
     *            hit
     */
    private void mouseEvent(final Point point, final boolean ctrlDown, final boolean rightMouse) {
        if (!rightMouse) {
            String name = findTerritoryName(point);
            // name = JOptionPane.showInputDialog(this,
            // "Enter the territory name:", name);
            // if (name.trim().length() == 0)
            // return;
            // if (m_centers.containsKey(name)
            // && JOptionPane
            // .showConfirmDialog(this,
            // "Another center exists with the same name. Are you sure you want to replace it with this one?")
            // != 0)
            // return;
            // m_centers.put(name, point);
            if (conn == null) {
                conn = new Connection();
                conn.start = name;
                conn.sp = point;
            } else {
                conn.end = name;
                conn.ep = point;
                m_conns.add(new Connection(conn.start, conn.end, conn.sp, conn.ep));
                conn = null;
            }
        } else {
            // String centerClicked = null;
            // for (final Entry<String, Point> cur : m_centers.entrySet()) {
            // if (new Rectangle(cur.getValue(), new Dimension(15,
            // 15)).intersects(new Rectangle(point, new Dimension(
            // 1, 1))))
            // centerClicked = cur.getKey();
            // }
            // if (centerClicked != null
            // && JOptionPane.showConfirmDialog(this,
            // "Are you sure you want to remove this center?") == 0)
            // m_centers.remove(centerClicked);
        }
        repaint();
    }
}// end class CenterPicker
