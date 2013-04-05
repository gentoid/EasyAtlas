package ru.osm.dkiselev.atlasgenerator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public class MapRasterDataSource {
	
	private int dpi;
	private String wmsURL;
	private int widthPX;
	private int heightPX;
	
	private static final Logger logger = Logger.getLogger(MapRasterDataSource.class.getName());
	
	public MapRasterDataSource(int dpi, String wmsURL, int widthPX, int heightPX) {
		this.dpi = dpi;
		this.wmsURL = wmsURL;
		this.widthPX = widthPX;
		this.heightPX = heightPX;
	}

	public byte[] loadImage(GridCell c) {
		
		String url = getURL(c);
		logger.info("Load image: " + url);
		
		try {
			InputStream is = new URL(url).openStream();
			return IOUtils.toByteArray(is);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private String getURL(GridCell c) {
		StringBuilder sb = new StringBuilder(wmsURL);
		
		BBOX bbox = Calculator.forwardMercator(c.getBbox());
		
		sb.append("&bbox=");
		sb.append(bbox.getLeft());
		sb.append(",");
		sb.append(bbox.getBottom());
		sb.append(",");
		sb.append(bbox.getRight());
		sb.append(",");
		sb.append(bbox.getTop());
		
		sb.append("&dpi=").append(dpi);

		sb.append("&service=WMS");
		sb.append("&request=GetMap");
		sb.append("&version=1.1.1");
		sb.append("&layers=bw");
		sb.append("&format=image/png");
		sb.append("&height=").append(heightPX);
		sb.append("&width=").append(widthPX);
		sb.append("&srs=EPSG:3857");
		
		return sb.toString();
	}
	
	
}
