package com.supermap.desktop.process.graphics;

import com.supermap.desktop.process.events.GraphSelectChangedListener;
import com.supermap.desktop.process.events.GraphSelectedChangedEvent;
import com.supermap.desktop.process.graphics.connection.GraphRelationLine;
import com.supermap.desktop.process.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedListener;
import com.supermap.desktop.process.graphics.events.GraphCreatingEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatingListener;
import com.supermap.desktop.process.graphics.graphs.EllipseGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.ProcessGraph;
import com.supermap.desktop.process.graphics.graphs.RectangleGraph;
import com.supermap.desktop.process.graphics.interaction.canvas.*;
import com.supermap.desktop.process.graphics.interaction.graph.DefaultGraphEventHanderFactory;
import com.supermap.desktop.process.graphics.interaction.graph.IGraphEventHandlerFactory;
import com.supermap.desktop.process.graphics.painter.DefaultGraphPainterFactory;
import com.supermap.desktop.process.graphics.painter.IGraphPainterFactory;
import com.supermap.desktop.process.graphics.storage.IGraphConnection;
import com.supermap.desktop.process.graphics.storage.IGraphStorage;
import com.supermap.desktop.process.graphics.storage.ListGraphConnection;
import com.supermap.desktop.process.graphics.storage.ListGraphs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/1/17.
 * 画布单位1默认与屏幕像素1相等，画布缩放之后之后的画布单位1则与屏幕像素 1*scale 相等
 * 使用多套数据结构来进行元素的存储，比如是用 List 来进行元素的存储，使用四叉树来做空间关系的存储，使用暂未定的某种结构存储连接关系等
 * 图上流程在运行的时候解析为邻接矩阵，任务运行模块查找所有起点，同时开始执行，遇到等待状态的节点则等待，条件达成继续执行。（最简单的执行方案，无需特定结构存储执行过程）
 * 几种行为以及对应的事件需求
 * 1. 创建一个元素（MouseClicked）
 * 2. 选择元素（MouseClicked/MouseDragged）
 * 3. 拖拽元素（MosueClicked MouseMoved/MouseDragged）
 * 4. 连接元素（MouseClicked MouseMoved/MosueDragged）
 * 5. hot 元素（MouseMoved）
 * 优先级：创建 - 拖拽/连接 - 选择 - hot
 */
public class GraphCanvas extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener, KeyListener {
	public final static Color DEFAULT_BACKGROUNDCOLOR = new Color(11579568);
	public final static Color DEFAULT_CANVAS_COLOR = new Color(255, 255, 255);
	public final static Color GRID_MINOR_COLOR = new Color(15461355);
	public final static Color GRID_MAJOR_COLOR = new Color(13290186);

	private Rectangle canvasRect = new Rectangle(-2000, -2000, 4000, 4000);
	private IGraphStorage graphStorage = new ListGraphs(); // 画布元素的存储结构
	private CoordinateTransform coordinateTransform = new CoordinateTransform(this); // 用以在画布平移、缩放等操作过后进行坐标转换
	private IGraphPainterFactory painterFactory = new DefaultGraphPainterFactory(this); // 元素绘制的可扩展类
	private IGraphEventHandlerFactory graphHandlerFactory = new DefaultGraphEventHanderFactory(); // 在某具体元素上进行的可扩展交互类
	private ConcurrentHashMap<Class, CanvasEventHandler> canvasHandlers = new ConcurrentHashMap<>(); // 统一入口的画布事件接口，通过添加 CanvasEventHandler 对象实现 Canvas 的事件处理
	private IGraphConnection connection = new ListGraphConnection(this);

	private CanvasTranslation translation = new CanvasTranslation(this);
	private GraphCreator creator = new GraphCreator(this);
	private Selection selection = new MultiSelction(this);
	private DraggedHandler dragged = new DraggedHandler(this);
	public GraphConnector connector = new GraphConnector(this);
	public GraphRemoving removing = new GraphRemoving(this);

	private ArrayList<GraphSelectChangedListener> selectChangedListeners = new ArrayList<>();

	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		frame.setSize(1000, 650);
		ScrollGraphCanvas scrollCanvas = new ScrollGraphCanvas();
		final GraphCanvas canvas = scrollCanvas.getCanvas();

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(scrollCanvas, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JButton button = new JButton("Rectangle");
		panel.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RectangleGraph graph = new RectangleGraph(canvas);
				canvas.creator.create(graph);
			}
		});

		JButton button1 = new JButton("Ellipse");
		panel.add(button1);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EllipseGraph graph = new EllipseGraph(canvas);
				canvas.creator.create(graph);
			}
		});

		JButton button2 = new JButton("Process");
		panel.add(button2);
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProcessGraph graph = new ProcessGraph(canvas, null);
				graph.setSize(200, 80);
				graph.setArcHeight(10);
				graph.setArcWidth(10);
				canvas.creator.create(graph);
			}
		});

		JButton button3 = new JButton("Data");
		panel.add(button3);
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.connector.connecting();
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	public GraphCanvas() {
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		addKeyListener(this);

		setRequestFocusEnabled(true);

		installCanvasEventHandler(Selection.class, this.selection);
		installCanvasEventHandler(DraggedHandler.class, this.dragged);
		installCanvasEventHandler(CanvasTranslation.class, this.translation);
		installCanvasEventHandler(GraphCreator.class, this.creator);
		installCanvasEventHandler(GraphConnector.class, this.connector);
		installCanvasEventHandler(GraphRemoving.class, this.removing);
	}

	public void create(IGraph graph) {
		this.creator.create(graph);
	}

	public void installCanvasEventHandler(Class c, CanvasEventHandler handler) {
		if (c == null) {
			return;
		}

		if (this.canvasHandlers.contains(c)) {
			this.canvasHandlers.get(c).clean();
		}

		this.canvasHandlers.put(c, handler);
	}

	public void installCanvasEventHandler(CanvasEventHandler handler) {
		if (handler == null) {
			return;
		}

		Class c = handler.getClass();
		installCanvasEventHandler(c, handler);
	}

	public CanvasEventHandler getEventHandler(Class c) {
		if (this.canvasHandlers.size() > 0 && this.canvasHandlers.containsKey(c)) {
			return this.canvasHandlers.get(c);
		} else {
			return null;
		}
	}

	public void setEventHandlerEnabled(Class c, boolean enabled) {
		CanvasEventHandler handler = getEventHandler(c);
		if (handler != null) {
			handler.setEnabled(enabled);
		}
	}

	public CoordinateTransform getCoordinateTransform() {
		return coordinateTransform;
	}

	public void setCoordinateTransform(CoordinateTransform coordinateTransform) {
		this.coordinateTransform = coordinateTransform;
	}

	public IGraphStorage getGraphStorage() {
		return graphStorage;
	}

	public void setGraphStorage(IGraphStorage graphStorage) {
		this.graphStorage = graphStorage;
	}

	public IGraphPainterFactory getPainterFactory() {
		return painterFactory;
	}

	public void setPainterFactory(IGraphPainterFactory painterFactory) {
		this.painterFactory = painterFactory;
	}

	public void setSelectedDecorator(IGraph selectedDecorator) {

	}

	public void setHotDecorator(IGraph hotDecorator) {

	}

	public void setPreviewDecorator(IGraph previewDecorator) {

	}

	public Selection getSelection() {
		return this.selection;
	}

	public IGraphConnection getConnection() {
		return this.connection;
	}

	public void addGraphTransformed(IGraph graph) {
		if (graph != null && !this.graphStorage.contains(graph)) {
			this.coordinateTransform.inverseTranslate(graph);

			if (this.canvasRect.contains(graph.getBounds())) {
				fireGraphCreating(new GraphCreatingEvent(this, graph));
				this.graphStorage.add(graph);
				fireGraphCreated(new GraphCreatedEvent(this, graph));
			}
		}
	}

	public void addGraph(IGraph graph) {
		if (graph != null && !this.graphStorage.contains(graph)) {
			if (this.canvasRect.contains(graph.getBounds())) {
				fireGraphCreating(new GraphCreatingEvent(this, graph));
				this.graphStorage.add(graph);
				fireGraphCreated(new GraphCreatedEvent(this, graph));
			}
		}
	}

	public void removeGraph(IGraph graph) {
		if (graph != null && this.graphStorage.contains(graph)) {
			this.connection.removeConnection(graph);

			if (this.selection.isSelected(graph)) {
				this.selection.deselectItem(graph);
			}
			this.graphStorage.remove(graph);
		}
		repaint();
	}

	public void removeGraphs(IGraph[] graphs) {
		if (graphs != null && graphs.length > 0) {
			this.selection.deselectItems(graphs);
			for (int i = 0; i < graphs.length; i++) {
				removeGraph(graphs[i]);
			}
		}
		repaint();
	}

	public IGraph findGraph(Point screenPoint) {
		if (this.graphStorage != null && this.graphStorage.getCount() > 0) {
			Point canvasPoint = this.coordinateTransform.inverse(screenPoint);
			return this.graphStorage.findGraph(canvasPoint);
		} else {
			return null;
		}
	}

	public void modifyGraphBounds(IGraph graph, int x, int y, int width, int height) {
		this.graphStorage.modifyGraphBounds(graph, x, y, width, height);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;
		setViewRenderingHints(graphics2D);
		paintBackground(graphics2D);
		paintCanvas(graphics2D);

		AffineTransform origin = graphics2D.getTransform();
		graphics2D.setTransform(this.coordinateTransform.getAffineTransform(origin));
		paintGraphs(graphics2D);
		paintLines(graphics2D);
		this.connector.preview(graphics2D);
		this.selection.paintSelected(graphics2D);
		graphics2D.setTransform(origin);

		this.creator.paint(graphics2D);
		this.selection.paint(graphics2D);
	}

	private int getScale(int i) {
		return i * 100;
	}

	/**
	 * 绘制背景
	 *
	 * @param g
	 */
	private void paintBackground(Graphics2D g) {
		g.setColor(DEFAULT_BACKGROUNDCOLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * 绘制画布
	 *
	 * @param g
	 */
	private void paintCanvas(Graphics2D g) {
		Rectangle rect = this.coordinateTransform.transform(getCanvasRect());
		g.setColor(DEFAULT_CANVAS_COLOR);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}

	private void paintGraphs(Graphics2D g) {
		IGraph[] graphs = this.graphStorage.getGraphs();
		for (int i = 0; i < graphs.length; i++) {
			IGraph graph = graphs[i];
			this.painterFactory.getPainter(graph, g).paint();
		}
	}

	private void paintLines(Graphics2D g) {
		GraphRelationLine[] lines = this.connection.getLines();
		for (int i = 0, n = lines.length; i < n; i++) {
			lines[i].paint(g);
		}
	}

	/**
	 * 返回画布的可见区域，画布坐标单位
	 *
	 * @return
	 */
	public Rectangle getVisibleCanvasRect() {
		return this.coordinateTransform.inverse(getVisibleRect());
	}

	/**
	 * 获取画布尺寸，画布坐标单位
	 *
	 * @return
	 */
	public Rectangle getCanvasRect() {
		return this.canvasRect;
	}

	/**
	 * 这是个借鉴方法，大约是一些抗锯齿的设置
	 *
	 * @param g
	 */
	private void setViewRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}

	private Point2D panelToCanvas(Point point) {
		return new Point2D.Double(point.getX(), point.getY());
	}

	private Point canvasToPanel(Point2D point2D) {
		int panelX = Double.valueOf(point2D.getX()).intValue();
		int panelY = Double.valueOf(point2D.getY()).intValue();
		return new Point(panelX, panelY);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseClicked(e);
			}
		}
	}

	public IGraph[] findGraphs(Point point) {
		return this.graphStorage.findGraphs(point);
	}

	public IGraph[] findContainedGraphs(int x, int y, int width, int height) {
		Rectangle canvasRect = this.coordinateTransform.inverse(x, y, width, height);
		return this.graphStorage.findContainedGraphs(canvasRect.x, canvasRect.y, canvasRect.width, canvasRect.height);
	}

	public IGraph[] findIntersectedGraphs(int x, int y, int width, int height) {
		Rectangle canvasRect = this.coordinateTransform.inverse(x, y, width, height);
		return this.graphStorage.findIntersetctedGraphs(canvasRect.x, canvasRect.y, canvasRect.width, canvasRect.height);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (!hasFocus() && isRequestFocusEnabled()) {
				requestFocus();
			}
		}

		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mousePressed(e);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseReleased(e);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseEntered(e);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseExited(e);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseDragged(e);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseMoved(e);
			}
		}
	}

	private void repaint(IGraph graph, Point point) {
		if (graph.getLocation() != point) {
			Rectangle dirtyRect = graph.getBounds();
			double x = point.getX() - graph.getWidth() / 2;
			double y = point.getY() - graph.getHeight() / 2;
			Point location = new Point();
			location.setLocation(x, y);
			graph.setLocation(location);
			repaint(dirtyRect);
			repaint(graph.getBounds());
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().mouseWheelMoved(e);
			}
		}
	}

	public void addGraphSelectChangedListener(GraphSelectChangedListener listener) {
		if (!this.selectChangedListeners.contains(listener)) {
			this.selectChangedListeners.add(listener);
		}
	}

	public void removeGraphSelectChangedListener(GraphSelectChangedListener listener) {
		if (this.selectChangedListeners.contains(listener)) {
			this.selectChangedListeners.remove(listener);
		}
	}

	protected void fireGraphSelectChanged(GraphSelectedChangedEvent e) {
		for (int i = 0; i < this.selectChangedListeners.size(); i++) {
			this.selectChangedListeners.get(i).graphSelectChanged(e);
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

	public void addGraphCreatedListener(GraphCreatedListener listener) {
		this.listenerList.add(GraphCreatedListener.class, listener);
	}

	public void removeGraphCreatedListener(GraphCreatedListener listener) {
		this.listenerList.remove(GraphCreatedListener.class, listener);
	}

	public void addGraphCreatingListener(GraphCreatingListener listener) {
		this.listenerList.add(GraphCreatingListener.class, listener);
	}

	public void removeGraphCreatingListener(GraphCreatingListener listener) {
		this.listenerList.remove(GraphCreatingListener.class, listener);
	}

	private void fireGraphCreated(GraphCreatedEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphCreatedListener.class) {
				((GraphCreatedListener) listeners[i + 1]).graphCreated(e);
			}
		}
	}

	private void fireGraphCreating(GraphCreatingEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == GraphCreatingListener.class) {
				((GraphCreatingListener) listeners[i + 1]).graphCreating(e);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().keyTyped(e);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().keyPressed(e);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().isEnabled()) {
				entry.getValue().keyReleased(e);
			}
		}
	}
}
