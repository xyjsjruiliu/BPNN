package com.macb.main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartUtilities;

import com.macb.bp.BP;
import com.macb.chart.ResultChart;
import com.macb.file.CalFeatureThread;
import com.macb.file.FileUtils;
import com.macb.image.ImageProcess;
import com.macb.utils.AI;
import com.macb.utils.Utils;

public class MainFrame {

	private String fileName = null;

	private Image srcimage = null;
	private Image dstimage = null;

	private Canvas srccanvas;
	private Canvas dstcanvas;

	private Button buttonScane;
	private Button buttonRotate;
	private Button buttonCvtGray;
	private Button buttonQuantify;
	private Button buttonInitFeature;
	private Button buttonTrainSample;
	private Button buttonTest;
	private Button buttonLoadImage;
	private Button buttonResult;

	private Button buttonout;
	
	private Label resLabel;

	public static List<String> imagelist;
	public static List<String> sampleimagelist;
	public static List<String> indoorimagelist;
	public static List<String> outdoorimagelist;

	public static boolean isFeatured = false;
	public static boolean isTrained = false;

	public static int TRAIN_TIME = 400;
	public static int SAMPLE_NUMBER = 30;

	public static BP bp = new BP(20, 15, 1);

	public static void main(String[] args) {
		MainFrame frmae = new MainFrame();
	try {
		frmae.open();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	public void init(Display display) {
		imagelist = new FileUtils().getImageList(AI.IMAGE_DB_PATH, AI.ALL);
		indoorimagelist = new FileUtils().getImageList(AI.IMAGE_DB_PATH,
				AI.INDOOR);
		outdoorimagelist = new FileUtils().getImageList(AI.IMAGE_DB_PATH,
				AI.OUTDOOR);
	}

	public void open() throws IOException {
		final Display display = Display.getDefault();
		final Shell shell = new Shell();
		shell.setText("\"室内景物\"和\"室外景物\"图像分类");
		shell.setSize(835, 490);
		init(display);

		shell.open();

		buttonInitFeature = new Button(shell, SWT.NONE);
		buttonInitFeature.setText("初始化(第一步)");
		buttonInitFeature.setBounds(10, 5, 85, 25);

		buttonTrainSample = new Button(shell, SWT.NONE);
		buttonTrainSample.setText("BP训练(第二步)");
		buttonTrainSample.setBounds(100, 5, 85, 25);

		buttonLoadImage = new Button(shell, SWT.NONE);
		buttonLoadImage.setText("随机选取图片");
		buttonLoadImage.setBounds(190, 5, 80, 25);

		buttonTest = new Button(shell, SWT.NONE);
		buttonTest.setText("测试");
		buttonTest.setBounds(280, 5, 80, 25);

		buttonResult = new Button(shell, SWT.NONE);
		buttonResult.setText("结果统计");
		buttonResult.setBounds(370, 5, 80, 25);

		buttonScane = new Button(shell, SWT.NONE);
		buttonScane.setText("手动选取图片");
		buttonScane.setBounds(460, 5, 80, 25);



		buttonCvtGray = new Button(shell, SWT.NONE);
		buttonCvtGray.setText("灰度");
		buttonCvtGray.setBounds(550, 5, 80, 25);

		buttonQuantify = new Button(shell, SWT.NONE);
		buttonQuantify.setText("量化");
		buttonQuantify.setBounds(640, 5, 80, 25);
		
		
		buttonout=new Button(shell,SWT.FLAT);
		buttonout.setText("退出系统");
		buttonout.setBounds(730,5,80,25);
		
		
		
		resLabel = new Label(shell, SWT.None);
		resLabel.setBounds(10, 430, 200, 25);

		srccanvas = new Canvas(shell, SWT.FLAT);
		srccanvas.setBounds(10, 40, 384, 384);

		dstcanvas = new Canvas(shell, SWT.FLAT);
		dstcanvas.setBounds(421, 40, 384, 384);

		
		
		
		buttonInitFeature.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				resLabel.setText("正在特征初始化,请稍后>>>");
				if (imagelist.size() == 0 | imagelist == null) {
					resLabel.setForeground(new Color(display, 255, 0, 0));
					resLabel.setText("加载图像数据库失败...");
					return;
				}
				CalFeatureThread cfthread = new CalFeatureThread(display,
						imagelist);
				cfthread.start();
				try {
					cfthread.join();
					isFeatured = true;
					resLabel.setForeground(new Color(display, 0, 0, 0));
					resLabel.setText("特征初始化完成...");
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		buttonTrainSample.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				resLabel.setText("正在训练,请稍后>>>");
				if (imagelist.size() == 0 | imagelist == null
						|| isFeatured == false) {
					resLabel.setForeground(new Color(display, 255, 0, 0));
					resLabel.setText("请先初始化特征...");
					return;
				}
				int[] indeice = new Utils().getRandomArray(1, 100,
						SAMPLE_NUMBER);
				for (int k = 0; k < TRAIN_TIME; k++) { // 训练200次
					for (int i = 0; i < indeice.length; i++) {
						double fv[] = new FileUtils()
								.getFeatureFromFile(indoorimagelist
										.get(indeice[i]));
						bp.train(fv, new double[] { 1.0 });
					}
					for (int i = 0; i < indeice.length; i++) {
						double fv[] = new FileUtils()
								.getFeatureFromFile(outdoorimagelist
										.get(indeice[i]));
						bp.train(fv, new double[] { 0.0 });
					}
				}
				resLabel.setText("正在训练，请稍后");
				resLabel.setForeground(new Color(display, 0, 0, 0));
				resLabel.setText("网络训练完成...");
			}
		});

		buttonLoadImage.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				resLabel.setText("");
				int index = (int) (Math.random() * imagelist.size());
				srcimage = new Image(display, imagelist.get(index));
				srccanvas.redraw();

			}
		});

		buttonTest.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {

				if (srcimage == null) {
					resLabel.setForeground(new Color(display, 255, 0, 0));
					resLabel.setText("请选择要训练的图片...");
					return;
				}
				if (isFeatured == false) {
					resLabel.setForeground(new Color(display, 255, 0, 0));
					resLabel.setText("请先训练神经网络...");
					return;
				}
				double fv[] = new ImageProcess().getUnitFeature(srcimage
						.getImageData());
				int testResult = new BigDecimal(bp.test(fv)[0]).setScale(0,
						BigDecimal.ROUND_HALF_UP).intValue();
				if (testResult == 1) {
					resLabel.setForeground(new Color(display, 255, 0, 0));
					resLabel.setText("这是一张室内图...");
				} else {
					resLabel.setForeground(new Color(display, 255, 0, 0));
					resLabel.setText("这是一张室外图...");
				}
			}
		});
		
		buttonout.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(final SelectionEvent e) {
				
				
			System.exit(0);
				
			}
			
			
			
			
			
			
		});

		buttonResult.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {

				double[] fv = null;
				int testResult = 0;

				int idt = 0;
				int idf = 0;
				int odt = 0;
				int odf = 0;

				FileWriter fw = null;
				BufferedWriter bw = null;
				try {
					fw = new FileWriter("log.txt", false);
					bw = new BufferedWriter(fw);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}

				for (int i = 0; i < imagelist.size(); i++) {

					Image img = new Image(display, imagelist.get(i));
					fv = new ImageProcess().getUnitFeature(img.getImageData());
					testResult = new BigDecimal(bp.test(fv)[0]).setScale(0,
							BigDecimal.ROUND_HALF_UP).intValue();
					try {
						bw
								.write(imagelist.get(i)
										+ (testResult == 1 ? "---Indoor"
												: "---Outdoor"));
						bw.newLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (imagelist.get(i).contains("Indoor")) {
						if (testResult == 1) {
							idt++;
						} else {
							idf++;
						}
					} else {
						if (testResult == 0) {
							odt++;
						} else {
							odf++;
						}
					}
				}
				try {
					bw.flush();
					bw.close();
					fw.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				// System.out.println(idt + " " + idf + " " + odt + " " + odf);

				ResultChart rc = new ResultChart(
						new int[] { idt, idf, odt, odf });

				try {
					OutputStream os = new FileOutputStream("result.jpeg");
					ChartUtilities
							.writeChartAsJPEG(os, rc.getChart(), 384, 290);
					dstimage = new Image(display, "result.jpeg");
					dstcanvas.redraw();
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		});
		buttonScane.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {

				FileDialog dlg = new FileDialog(shell, SWT.OPEN);
				resLabel.setText("");
				dlg.setText("Open");
				dlg.setFilterNames(new String[] { "图片文件(*.jpg)",
								"图片文件（*.gif）" });
				dlg.setFilterExtensions(new String[] { "*.jpg", "*.gif",
						"*.png" });
				fileName = dlg.open();
				if (fileName != null) {
					srcimage = new Image(display, fileName);
					srccanvas.redraw();
				}
			}
		});

	

		buttonCvtGray.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if (srcimage == null)
					return;
				ImageData srcData = srcimage.getImageData();
				srcData = new ImageProcess().toGrayImage(srcData);
				dstimage = new Image(display, srcData);
				dstcanvas.redraw();
			}
		});

		buttonQuantify.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(final SelectionEvent e) {
				if (srcimage == null)
					return;
				ImageData srcData = srcimage.getImageData();
				srcData = new ImageProcess().toQuantifiedImage(srcData);
				dstimage = new Image(display, srcData);
				dstcanvas.redraw();
			}
		});

		srccanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent arg0) {
				if (srcimage != null) {
					if (srcimage.getBounds().height > srcimage.getBounds().width)
						arg0.gc.drawImage(srcimage,
								(384 - srcimage.getBounds().width) / 2, 0);
					else {
						arg0.gc.drawImage(srcimage, 0, (384 - srcimage
								.getBounds().height) / 2);
					}
				}
			}
		});

		dstcanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent arg0) {
				if (dstimage != null) {
					if (dstimage.getBounds().height > dstimage.getBounds().width)
						arg0.gc.drawImage(dstimage,
								(384 - dstimage.getBounds().width) / 2, 0);
					else {
						arg0.gc.drawImage(dstimage, 0, (384 - dstimage
								.getBounds().height) / 2);
					}
				}
			}
		});

		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
