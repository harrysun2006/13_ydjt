package com.free.util;

import static nl.captcha.util.ImageUtil.applyFilter;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import nl.captcha.Captcha;
import nl.captcha.Captcha.Builder;
import nl.captcha.backgrounds.BackgroundProducer;
import nl.captcha.backgrounds.FlatColorBackgroundProducer;
import nl.captcha.gimpy.GimpyRenderer;
import nl.captcha.gimpy.RippleGimpyRenderer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import nl.captcha.noise.NoiseProducer;
import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.producer.TextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;
import nl.captcha.text.renderer.WordRenderer;

import com.jhlabs.image.BentleyFilter;

public class CaptchaUtil {

  private static final char[] CAPTCHA_CHARS = "2345678".toCharArray();

  private CaptchaUtil() {

  }

  public static Captcha get() {
    Builder builder = new Builder(75, 25);
    NoiseProducer[] nps = new NoiseProducer[] { new CurvedLineNoiseProducer(Color.decode("0x00FF00"), 1),
    // new CurvedLineNoiseProducer(Color.decode("0x008000"), 1),
    };
    for (NoiseProducer np : nps)
      builder.addNoise(np);
    List<Font> fl = new ArrayList<Font>();
    Font f1 = new Font("SansSerif", Font.PLAIN, 20);
    AffineTransform at = new AffineTransform();
    at.setToScale(1.5, 1);
    fl.add(f1.deriveFont(at));
    // fl.add(new Font("Courier", Font.BOLD, 28));
    List<Color> cl = new ArrayList<Color>();
    cl.add(Color.decode("0x800000"));
    // cl.add(Color.decode("0x008000"));
    // cl.add(Color.decode("0x000080"));
    // cl.add(Color.BLACK);
    WordRenderer wr = new DefaultWordRenderer(cl, fl);

    TextProducer tp = new DefaultTextProducer(4, CAPTCHA_CHARS);
    builder.addText(tp, wr);
    // BackgroundProducer bp = new GradiatedBackgroundProducer(Color.WHITE,
    // Color.GRAY);
    BackgroundProducer bp = new FlatColorBackgroundProducer(Color.WHITE);
    builder.addBackground(bp);
    // 无渐进效果，只是填充背景颜色
    // FlatColorBackgroundProducer fbp = new
    // FlatColorBackgroundProducer(Color.red);
    // 加入网纹--一般不会用
    // SquigglesBackgroundProducer sbp = new SquigglesBackgroundProducer();
    // 没发现有什么用,可能就是默认的
    // TransparentBackgroundProducer tbp = new
    // TransparentBackgroundProducer();

    // 字体边框齿轮效果 默认是3
    // builder.gimp(new BlockGimpyRenderer(1));
    builder.gimp(new RippleGimpyRenderer());
    // 波纹渲染 相当于加粗
    // builder.gimp(new RippleGimpyRenderer());
    // 修剪--一般不会用
    // builder.gimp(new ShearGimpyRenderer(Color.red));
    // 加网--第一个参数是横线颜色，第二个参数是竖线颜色
    // builder.gimp(new FishEyeGimpyRenderer(Color.red,Color.yellow));
    // 加入阴影效果 默认3, 75
    // builder.gimp(new DropShadowGimpyRenderer());

    return builder.build();
  }

  public static final class BentleyGimpyRenderer implements GimpyRenderer {

    @Override
    public void gimp(BufferedImage bi) {
      BentleyFilter filter = new BentleyFilter();
      filter.setAmount(2);
      applyFilter(bi, filter);
    }

  }

}
