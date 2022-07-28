//package com.demo.demobase.config;
//
//import com.vividsolutions.jts.geom.Geometry;
//import com.vividsolutions.jts.geom.MultiLineString;
//import com.vividsolutions.jts.geom.MultiPolygon;
//import com.vividsolutions.jts.geom.Point;
//import com.vividsolutions.jts.io.ParseException;
//import com.vividsolutions.jts.io.WKTReader;
//import lombok.extern.log4j.Log4j2;
//import org.geotools.data.FeatureSource;
//import org.geotools.data.FeatureWriter;
//import org.geotools.data.Transaction;
//import org.geotools.data.shapefile.ShapefileDataStore;
//import org.geotools.data.shapefile.ShapefileDataStoreFactory;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.data.simple.SimpleFeatureIterator;
//import org.geotools.data.simple.SimpleFeatureStore;
//import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
//import org.geotools.geometry.jts.JTSFactoryFinder;
//import org.geotools.referencing.crs.DefaultGeographicCRS;
//import org.opengis.feature.Property;
//import org.opengis.feature.simple.SimpleFeature;
//import org.opengis.feature.simple.SimpleFeatureType;
//
//import java.io.*;
//import java.nio.charset.Charset;
//import java.util.*;
//
//@Log4j2
//public class ShapeTools {
//
//	private static final String GEOM_KEY = "the_geom";
//	private static final String PLOT_TYPE = "Coordinate"; //数据库里面用来表示坐标的属性
//
//	/**
//	 * 导出到shape文件
//	 *
//	 * @param exportName      文件名称
//	 * @param geomType        对象类型：point/shape
//	 * @param dataList        数据
//	 * @param coordinateField 坐标属性名
//	 * @return 导出并压缩后的文件路径
//	 */
//	public static File exportToShape(String exportName, String geomType, String coordinateField, List<LinkedHashMap<String, Object>> dataList) {
//		Set<String> keys = new LinkedHashSet<>(dataList.get(0).keySet());
//		keys.remove(coordinateField);
//		String uuid = CommonUtil.uuid();
//		String localDirPath = System.getProperty("java.io.tmpdir") + File.separator + "exp_" + uuid + File.separator + exportName;
//		File dirFile = new File(localDirPath);
//		if (!dirFile.exists()) {
//			dirFile.mkdirs();
//		}
//		File shpFile = new File(localDirPath + File.separator + exportName + ".shp");
//		generateCpgFile(localDirPath, exportName);
//		try {
//			Map<String, Serializable> params = new HashMap<>();
//			params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
//			ShapefileDataStore dataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
//			//定义图形信息和属性信息
//			dataStore.setStringCharset(Charset.forName("GBK"));
//			SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
//			simpleFeatureTypeBuilder.setCRS(DefaultGeographicCRS.WGS84);
//			simpleFeatureTypeBuilder.setName("shapeFile");
//			switch (geomType.toUpperCase()) {
//				case "POINT":
//					simpleFeatureTypeBuilder.add(GEOM_KEY, Point.class);
//					break;
//				case "LINE":
//					simpleFeatureTypeBuilder.add(GEOM_KEY, MultiLineString.class);
//					break;
//				case "POLYGON":
//					simpleFeatureTypeBuilder.add(GEOM_KEY, MultiPolygon.class);
//					break;
//				default:
//					throw new RuntimeException("空间字段类型错误");
//			}
//
//			for (String key : keys) {
//				simpleFeatureTypeBuilder.add(key, String.class);
//			}
//			dataStore.createSchema(simpleFeatureTypeBuilder.buildFeatureType());
//			//设置Writer
//			FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter(dataStore.getTypeNames()[0], Transaction.AUTO_COMMIT);
//			generateCoordinateData(coordinateField, dataList, writer);
//			writer.write();
//			writer.close();
//			dataStore.dispose();
//			File outFile = CompressionUtil.toZip(new File(localDirPath), true);
//			return outFile;
//		} catch (Exception e) {
//			log.error("exportToShape:", e);
//			return null;
//		}
//	}
//
//	/**
//	 * 设置字符编码 防止乱码
//	 * 创建cpg文件对象
//	 */
//	private static void generateCpgFile(String localDirPath, String cpgName) {
//		File file = new File(localDirPath + File.separator + cpgName + ".cpg");
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//			writer.write("GBK,GB2312,UTF-8");
//			writer.flush();
//		} catch (Exception e) {
//			log.error("generateCpgFile:", e);
//			e.printStackTrace();
//		}
//	}
//
//	private static void generateCoordinateData(String coordinateField, List<LinkedHashMap<String, Object>> dataList, FeatureWriter<SimpleFeatureType, SimpleFeature> writer) throws IOException, ParseException, ParseException {
//		SimpleFeature feature;
//		WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
//		for (Map<String, Object> map : dataList) {
//			feature = writer.next();
//			for (Map.Entry<String, Object> entry : map.entrySet()) {
//				String key = entry.getKey();
//				if (key == null) {
//					continue;
//				}
//				String value = entry.getValue() == null ? "" : entry.getValue().toString();
//				if (key.equals(coordinateField)) {
//					try {
//						//地理坐标的值在shp文件中需要用形状（如MULTIPOLYGON）标识出来，若传入的值只有坐标，需要转换成形状+坐标的格式，如MULTIPOLYGON(((100 100)))
//						Geometry geometry = wktReader.read(value);
//						feature.setAttribute(GEOM_KEY, geometry);
//					} catch (Exception e) {
//						log.error("generateCoordinateData", e);
//					}
//				} else {
//					//注意： value长度不能大于255个字符，如果大于，需要手动截取
//					feature.setAttribute(key, value);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 读取shape文件到集合中
//	 */
//	public static List<Map<String, Object>> readToArray(File shape) throws IOException {
//		List<Map<String, Object>> records = new LinkedList<>();
//		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
//		Map<String, Serializable> params = new HashMap<String, Serializable>();
//		params.put("url", shape.toURI().toURL());
//		params.put("create spatial index", Boolean.TRUE);
//		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
//		newDataStore.setStringCharset(Charset.forName("GBK"));
//		for (String typeName : newDataStore.getTypeNames()) {
//			FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = newDataStore.getFeatureSource(typeName);
//			if (featureSource instanceof SimpleFeatureStore) {
//				SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
//				SimpleFeatureCollection collection = featureStore.getFeatures();
//				SimpleFeatureIterator itertor = collection.features();
//				while (itertor.hasNext()) {
//					SimpleFeature feature = itertor.next();
//					Geometry geometry = (Geometry) feature.getDefaultGeometry();
//					Iterator<Property> it = feature.getProperties().iterator();
//					Map<String, Object> record = new LinkedHashMap<>();
//					records.add(record);
//					while (it.hasNext()) {
//						Property pro = it.next();
//						if (!pro.getName().toString().equalsIgnoreCase(GEOM_KEY)) {
//							String key = pro.getName().toString().toUpperCase();
//							Object value = pro.getValue();
//							record.put(key, value);
//						}
//					}
//					if (geometry == null) {
//						record.put("SHAPE", "");
//						continue;
//					}
//					int cnt = geometry.getNumGeometries();
//					if (cnt == 1) {
//						record.put("SHAPE", geometry.getGeometryN(0).toString());
//					}
//				}
//			}
//		}
//		return records;
//	}
//
//}
