package com.supermap.desktop.process.graphics;

import com.supermap.desktop.process.events.GraphSelectChangedListener;
import com.supermap.desktop.process.events.GraphSelectedChangedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatedListener;
import com.supermap.desktop.process.graphics.events.GraphCreatingEvent;
import com.supermap.desktop.process.graphics.events.GraphCreatingListener;
import com.supermap.desktop.process.graphics.graphs.EllipseGraph;
import com.supermap.desktop.process.graphics.graphs.IGraph;
import com.supermap.desktop.process.graphics.graphs.RectangleGraph;
import com.supermap.desktop.process.graphics.handler.canvas.CanvasEventHandler;
import com.supermap.desktop.process.graphics.handler.graph.DefaultGraphEventHanderFactory;
import com.supermap.desktop.process.graphics.handler.graph.IGraphEventHandlerFactory;
import com.supermap.desktop.process.graphics.interaction.CanvasTranslation;
import com.supermap.desktop.process.graphics.interaction.GraphCreation;
import com.supermap.desktop.process.graphics.interaction.MultiSelction;
import com.supermap.desktop.process.graphics.interaction.Selection;
import com.supermap.desktop.process.graphics.painter.DefaultGraphPainterFactory;
import com.supermap.desktop.process.graphics.painter.IGraphPainterFactory;
import com.supermap.desktop.process.graphics.storage.IGraphStorage;
import com.supermap.desktop.process.graphics.storage.ListGraphs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
public class GraphCanvas extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {
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

	private CanvasTranslation translation = new CanvasTranslation(this);
	private GraphCreation creation = new GraphCreation(this);
	private Selection selection = new MultiSelction(this);

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
				graph.setSize(200, 80);
				graph.setArcHeight(10);
				graph.setArcWidth(10);

				canvas.creation.create(graph);
			}
		});

		JButton button1 = new JButton("Ellipse");
		panel.add(button1);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EllipseGraph graph = new EllipseGraph(canvas);
				graph.setSize(160, 60);
				canvas.creation.create(graph);
			}
		});

		JButton button2 = new JButton("Process");
		panel.add(button2);
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				ProcessGraph graph = new ProcessGraph(canvas, null);
//				graph.setSize(200, 80);
//				graph.setArcHeight(10);
//				graph.setArcWidth(10);
			}
		});

		JButton button3 = new JButton("Data");
		panel.add(button3);
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Rectangle rect1 = new Rectangle(0, 0, 100, 100);
				Rectangle rect2 = new Rectangle(200, 150, 101, 101);
				System.out.println(rect1.union(rect2));
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
	}

	public void create(IGraph graph) {
		this.creation.create(graph);
	}

	public void installCanvasEventHandler(CanvasEventHandler handler) {
		if (handler == null) {
			return;
		}

		Class c = handler.getClass();
		if (c == null) {
			return;
		}

		if (this.canvasHandlers.contains(c)) {
			this.canvasHandlers.get(c).clean();
		}

		this.canvasHandlers.put(c, handler);
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

	public void addGraph(IGraph graph) {
		if (graph != null && !this.graphStorage.contains(graph)) {
			this.coordinateTransform.inverse(graph);

			if (this.canvasRect.contains(graph.getBounds())) {
				fireGraphCreating(new GraphCreatingEvent(this, graph));
				this.graphStorage.add(graph);
				fireGraphCreated(new GraphCreatedEvent(this, graph));
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D graphics2D = (Graphics2D) g;

//		int borderWidth = getScale(2);
//		int x = getScale(1);
//		int y = getScale(1);
//		int width = getScale(4);
//		int height = getScale(4);
//
//		graphics2D.setColor(Color.RED);
//		Stroke stroke = new BasicStroke(borderWidth);
//		graphics2D.setStroke(stroke);
//		Rectangle rectangle = new Rectangle(x, y, width, height);
//		graphics2D.draw(rectangle);


//		graphics2D.setColor(Color.RED);
//		Stroke stroke = new BasicStroke(4);
//		graphics2D.setStroke(stroke);
//		Rectangle rectangle = new Rectangle(3, 3, 5, 5);
//		graphics2D.draw(rectangle);


		// 测试 AffineTransform 的缩放和平移变换
//		AffineTransform transform = new AffineTransform();
//		setViewRenderingHints(graphics2D);
//		paintBackground(graphics2D);
//		paintCanvas(graphics2D);
//		transform.translate(100, 100);
//		transform.scale(2, 2);
//		graphics2D.setTransform(transform);
//
//		graphics2D.setColor(Color.ORANGE);
//		RoundRectangle2D round = new RoundRectangle2D.Double(100, 100, 300, 160, 30, 30);
//		graphics2D.fill(round);
//
//		graphics2D.setColor(Color.BLACK);
//		BasicStroke stroke = new BasicStroke(3);
//
//		graphics2D.setStroke(stroke);
//		graphics2D.draw(round);

		// Canvas 自身绘制
		setViewRenderingHints(graphics2D);
		paintBackground(graphics2D);
		paintCanvas(graphics2D);

		AffineTransform origin = graphics2D.getTransform();
		graphics2D.setTransform(this.coordinateTransform.getAffineTransform(origin));
		paintGraphs(graphics2D);
		graphics2D.setTransform(origin);

		this.creation.paint(graphics2D);
		this.selection.paint(graphics2D);

		// 默认 AffineTransform 的测试
//		graphics2D.setColor(Color.ORANGE);
//		RoundRectangle2D round1 = new RoundRectangle2D.Double(100, 100, 300, 160, 30, 30);
//		graphics2D.fill(round);
//
//		graphics2D.setColor(Color.BLACK);
//		BasicStroke stroke1 = new BasicStroke(3);
//
//		graphics2D.setStroke(stroke1);
//		graphics2D.draw(round1);
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
			entry.getValue().mouseClicked(e);
		}

		this.translation.mouseClicked(e);
		this.creation.mouseClicked(e);
		this.selection.mouseClicked(e);
	}


	public IGraph findTopGraph(Point point) {
		IGraph graph = null;
		IGraph[] graphs = this.graphStorage.findGraphs(point);

		if (graphs != null && graphs.length > 0) {
			graph = graphs[0];
		}
		return graph;
	}

	public IGraph[] findGraphs(Point point) {
		return this.graphStorage.findGraphs(point);
	}

	public IGraph[] findContainedGraphs(int x, int y, int width, int height) {
		return this.graphStorage.findContainedGraphs(x, y, width, height);
	}

	public IGraph[] findIntersectedGraphs(int x, int y, int width, int height) {
		return this.graphStorage.findIntersetctedGraphs(x, y, width, height);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().enable()) {
				entry.getValue().mousePressed(e);
			}
		}

		this.translation.mousePressed(e);
		this.creation.mousePressed(e);
		this.selection.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().enable()) {
				entry.getValue().mouseReleased(e);
			}
		}

		this.translation.mouseReleased(e);
		this.creation.mouseReleased(e);
		this.selection.mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().enable()) {
				entry.getValue().mouseEntered(e);
			}
		}

		this.translation.mouseEntered(e);
		this.creation.mouseEntered(e);
		this.selection.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().enable()) {
				entry.getValue().mouseExited(e);
			}
		}

		this.translation.mouseExited(e);
		this.creation.mouseExited(e);
		this.selection.mouseExited(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().enable()) {
				entry.getValue().mouseDragged(e);
			}
		}

		this.translation.mouseDragged(e);
		this.creation.mouseDragged(e);
		this.selection.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Set<Map.Entry<Class, CanvasEventHandler>> set = this.canvasHandlers.entrySet();
		Iterator<Map.Entry<Class, CanvasEventHandler>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<Class, CanvasEventHandler> entry = iterator.next();
			if (entry.getValue().enable()) {
				entry.getValue().mouseMoved(e);
			}
		}

		this.translation.mouseMoved(e);
		this.creation.mouseMoved(e);
		this.selection.mouseMoved(e);
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
			if (entry.getValue().enable()) {
				entry.getValue().mouseWheelMoved(e);
			}
		}

		this.translation.mouseWheelMoved(e);
		this.creation.mouseWheelMoved(e);
		this.selection.mouseWheelMoved(e);
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
}
