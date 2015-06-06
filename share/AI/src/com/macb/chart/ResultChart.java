package com.macb.chart;

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class ResultChart {

	private int[] res;

	public ResultChart(int[] res) {
		this.res = res;
	}

	private PieDataset getDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("ID(T):" + res[0], res[0]);
		dataset.setValue("ID(F):" + res[1], res[1]);
		dataset.setValue("OD(T):" + res[2], res[2]);
		dataset.setValue("OD(F):" + res[3], res[3]);
		return dataset;
	}

	public JFreeChart getChart() {

		JFreeChart chart = ChartFactory.createPieChart("分类结果统计图", getDataset(),
				true, true, false);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionOutlinesVisible(false);
		plot.setLabelFont(new Font("Consolas", Font.PLAIN, 12));
		plot.setCircular(true);
		plot.setLabelGap(0.02);
		return chart;
	}
}
