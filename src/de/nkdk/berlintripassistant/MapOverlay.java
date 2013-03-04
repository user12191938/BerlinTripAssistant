package de.nkdk.berlintripassistant;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class MapOverlay extends com.google.android.maps.Overlay {
	
	private Resources resources;
	private GeoPoint geoPoint;
	public MapOverlay(Resources resources, GeoPoint geoPoint) {
		this.resources = resources;
		this.geoPoint = geoPoint;
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
		super.draw(canvas, mapView, shadow);
		Paint paint = new Paint();
		Point screenPts = new Point();
		mapView.getProjection().toPixels(geoPoint, screenPts);
		paint.setStrokeWidth(1);
		paint.setARGB(255, 255, 255, 255);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		Bitmap bmp = BitmapFactory.decodeResource(resources, android.R.drawable.sym_def_app_icon);
		// bmp.eraseColor(Color.WHITE);
		canvas.drawBitmap(bmp, screenPts.x - 100, screenPts.y - 100, paint);
		return true;
	}
}