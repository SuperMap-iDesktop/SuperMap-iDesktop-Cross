package com.supermap.desktop.process;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.tool.AbstractTool;
import org.jhotdraw.draw.tool.CreationTool;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by highsad on 2017/1/17.
 */
public class SmCreationTool extends AbstractTool {

	protected Map<AttributeKey, Object> prototypeAttributes;
	protected String presentationName;
	protected Dimension minimalSizeTreshold;
	protected Dimension minimalSize;
	protected Figure prototype;
	protected Figure createdFigure;
	private boolean isToolDoneAfterCreation;

	public SmCreationTool(String prototypeClassName) {
		this((String)prototypeClassName, (Map)null, (String)null);
	}

	public SmCreationTool(String prototypeClassName, Map<AttributeKey, Object> attributes) {
		this((String)prototypeClassName, attributes, (String)null);
	}

	public SmCreationTool(String prototypeClassName, Map<AttributeKey, Object> attributes, String name) {
		this.minimalSizeTreshold = new Dimension(2, 2);
		this.minimalSize = new Dimension(40, 40);
		this.isToolDoneAfterCreation = true;

		try {
			this.prototype = (Figure)Class.forName(prototypeClassName).newInstance();
		} catch (Exception var6) {
			InternalError error = new InternalError("Unable to create Figure from " + prototypeClassName);
			error.initCause(var6);
			throw error;
		}

		this.prototypeAttributes = attributes;
		if(name == null) {
			ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
			name = labels.getString("edit.createFigure.text");
		}

		this.presentationName = name;
	}

	public SmCreationTool(Figure prototype) {
		this((Figure)prototype, (Map)null, (String)null);
	}

	public SmCreationTool(Figure prototype, Map<AttributeKey, Object> attributes) {
		this((Figure)prototype, attributes, (String)null);
	}

	/** @deprecated */
	public SmCreationTool(Figure prototype, Map<AttributeKey, Object> attributes, String name) {
		this.minimalSizeTreshold = new Dimension(2, 2);
		this.minimalSize = new Dimension(40, 40);
		this.isToolDoneAfterCreation = true;
		this.prototype = prototype;
		this.prototypeAttributes = attributes;
		if(name == null) {
			ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
			name = labels.getString("edit.createFigure.text");
		}

		this.presentationName = name;
	}

	public Figure getPrototype() {
		return this.prototype;
	}

	public void activate(DrawingEditor editor) {
		super.activate(editor);
		this.getView().setCursor(Cursor.getPredefinedCursor(1));
	}

	public void deactivate(DrawingEditor editor) {
		super.deactivate(editor);
		if(this.getView() != null) {
			this.getView().setCursor(Cursor.getDefaultCursor());
		}

		if(this.createdFigure != null) {
			if(this.createdFigure instanceof CompositeFigure) {
				((CompositeFigure)this.createdFigure).layout();
			}

			this.createdFigure = null;
		}

	}

	public void mousePressed(MouseEvent evt) {
		super.mousePressed(evt);
		this.getView().clearSelection();
		this.createdFigure = this.createFigure();
		Point2D.Double p = this.constrainPoint(this.viewToDrawing(this.anchor));
		this.anchor.x = evt.getX();
		this.anchor.y = evt.getY();
		this.createdFigure.setBounds(p, p);
		this.getDrawing().add(this.createdFigure);
	}

	public void mouseDragged(MouseEvent evt) {
		if(this.createdFigure != null) {
			Point2D.Double p = this.constrainPoint(new Point(evt.getX(), evt.getY()));
			this.createdFigure.willChange();
			this.createdFigure.setBounds(this.constrainPoint(new Point(this.anchor.x, this.anchor.y)), p);
			this.createdFigure.changed();
		}

	}

	public void mouseReleased(MouseEvent evt) {
		if(this.createdFigure != null) {
			java.awt.geom.Rectangle2D.Double bounds = this.createdFigure.getBounds();
			if(bounds.width == 0.0D && bounds.height == 0.0D) {
				this.getDrawing().remove(this.createdFigure);
				if(this.isToolDoneAfterCreation()) {
					this.fireToolDone();
				}
			} else {
				if(Math.abs(this.anchor.x - evt.getX()) < this.minimalSizeTreshold.width && Math.abs(this.anchor.y - evt.getY()) < this.minimalSizeTreshold.height) {
					this.createdFigure.willChange();
					this.createdFigure.setBounds(this.constrainPoint(new Point(this.anchor.x, this.anchor.y)), this.constrainPoint(new Point(this.anchor.x + (int)Math.max(bounds.width, (double)this.minimalSize.width), this.anchor.y + (int)Math.max(bounds.height, (double)this.minimalSize.height))));
					this.createdFigure.changed();
				}

				if(this.createdFigure instanceof CompositeFigure) {
					((CompositeFigure)this.createdFigure).layout();
				}

				final Figure addedFigure = this.createdFigure;
				final Drawing addedDrawing = this.getDrawing();
				this.getDrawing().fireUndoableEditHappened(new AbstractUndoableEdit() {
					public String getPresentationName() {
						return SmCreationTool.this.presentationName;
					}

					public void undo() throws CannotUndoException {
						super.undo();
						addedDrawing.remove(addedFigure);
					}

					public void redo() throws CannotRedoException {
						super.redo();
						addedDrawing.add(addedFigure);
					}
				});
				Rectangle r = new Rectangle(this.anchor.x, this.anchor.y, 0, 0);
				r.add(evt.getX(), evt.getY());
				this.maybeFireBoundsInvalidated(r);
				this.creationFinished(this.createdFigure);
				this.createdFigure = null;
			}
		} else if(this.isToolDoneAfterCreation()) {
			this.fireToolDone();
		}

	}

	protected Figure createFigure() {
		Figure f = (Figure)this.prototype.clone();
		this.getEditor().applyDefaultAttributesTo(f);
		if(this.prototypeAttributes != null) {
			Iterator i$ = this.prototypeAttributes.entrySet().iterator();

			while(i$.hasNext()) {
				Map.Entry entry = (Map.Entry)i$.next();
				f.set((AttributeKey)entry.getKey(), entry.getValue());
			}
		}

		return f;
	}

	protected Figure getCreatedFigure() {
		return this.createdFigure;
	}

	protected Figure getAddedFigure() {
		return this.createdFigure;
	}

	protected void creationFinished(Figure createdFigure) {
		if(createdFigure.isSelectable()) {
			this.getView().addToSelection(createdFigure);
		}

		if(this.isToolDoneAfterCreation()) {
			this.fireToolDone();
		}

	}

	public void setToolDoneAfterCreation(boolean newValue) {
		boolean oldValue = this.isToolDoneAfterCreation;
		this.isToolDoneAfterCreation = newValue;
	}

	public boolean isToolDoneAfterCreation() {
		return this.isToolDoneAfterCreation;
	}

	public void updateCursor(DrawingView view, Point p) {
		if(view.isEnabled()) {
			view.setCursor(Cursor.getPredefinedCursor(1));
		} else {
			view.setCursor(Cursor.getPredefinedCursor(3));
		}

	}
}
