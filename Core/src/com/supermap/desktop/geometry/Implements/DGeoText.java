package com.supermap.desktop.geometry.Implements;

import com.supermap.data.GeoText;
import com.supermap.data.Geometry;
import com.supermap.data.TextPart;
import com.supermap.data.TextStyle;
import com.supermap.desktop.Application;
import com.supermap.desktop.geometry.Abstract.AbstractGeometry;
import com.supermap.desktop.geometry.Abstract.IMultiPartFeature;
import com.supermap.desktop.geometry.Abstract.ITextFeature;

public class DGeoText extends AbstractGeometry implements ITextFeature, IMultiPartFeature<TextPart> {

	private GeoText geoText;

	protected DGeoText(GeoText geoText) {
		super(geoText);
		this.geoText = geoText;
	}

	@Override
	public int getPartCount() {
		return this.geoText == null ? -1 : this.geoText.getPartCount();
	}

	@Override
	public TextPart getPart(int index) {
		return this.geoText == null ? null : this.geoText.getPart(index);
	}

	@Override
	public void addPart(TextPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPart(Geometry geometry) {
		try {
			if (this.geoText != null && geometry instanceof GeoText) {
				GeoText geoText = (GeoText) geometry;
				TextStyle textStyle = geoText.getTextStyle();

				for (int i = 0; i < geoText.getPartCount(); i++) {
					this.geoText.addPart(geoText.getPart(i));
				}
				this.geoText.setTextStyle(textStyle);
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			if (geometry != null) {
				geometry.dispose();
			}
		}
	}

	@Override
	public Geometry[] divide() {
		if (this.geoText != null) {
			TextStyle textStyle = this.geoText.getTextStyle();
			Geometry[] geometries = new Geometry[this.geoText.getPartCount()];

			for (int i = 0; i < this.geoText.getPartCount(); i++) {
				geometries[i] = new GeoText(this.geoText.getPart(i), textStyle.clone());
			}
			return geometries;
		}

		return null;
	}
}
