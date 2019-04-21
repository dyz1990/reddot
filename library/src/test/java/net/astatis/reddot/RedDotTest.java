package net.astatis.reddot;

import android.support.annotation.NonNull;

import net.astatis.reddot.lib.RedDot;
import net.astatis.reddot.lib.RedDotManager;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by asia on 2017/7/12.
 */
public class RedDotTest {
    @Test
    public void findByName() throws Exception {
        RedDot redDot = init();

        RedDot child = redDot.getByName("a");
        Assert.assertNotNull(child);
        Assert.assertEquals(child.getName(), "a");


    }

    @Test
    public void findByTag() throws Exception {
        RedDot redDot = init();

        RedDot child = redDot.findByPath("a.e");
        Assert.assertNotNull(child);
        Assert.assertEquals(child.getName(), "e");
        Assert.assertEquals(child.getPathString(), "a.e");
    }

    @Test
    public void findOrCreateByTag() throws Exception {
        RedDot redDot = new RedDot("dot");
        redDot.findOrCreateByPath("a.b.c.e.f", true);
        redDot.findOrCreateByPath("a.b.m.e.f", true);
        redDot.findOrCreateByPath("a.d.m.e.f", true);
        redDot.findOrCreateByPath("c.b.m.n.g", true);
        redDot.findOrCreateByPath("c.a.m.n.f", true);

        RedDot child = redDot.findByPath("a.b");
        Assert.assertNotNull(child);
        Assert.assertEquals(child.getChildCount(), 2);
        RedDot adDot = redDot.findByPath("a.d");
        Assert.assertNotNull(adDot);
        Assert.assertEquals(adDot.getChildCount(), 1);
        final List<RedDot> leafRedDots = RedDotManager.findLeafRedDots(redDot);
        Assert.assertEquals(leafRedDots.size(), 5);
    }

    @Test
    public void setNumber() {
        RedDot redDot = new RedDot("dot");
        redDot.findOrCreateByPath("a.b.c.e.f", true).setNumber(1);
        redDot.findOrCreateByPath("a.b.m.e.f", true).setNumber(2);
        redDot.findOrCreateByPath("a.d.m.e.f", true).setNumber(2);
        redDot.findOrCreateByPath("c.b.m.n.g", true).setNumber(13);
        RedDot dot = redDot.findOrCreateByPath("c.a.m.n.f", true);
        dot.setNumber(34);

        Assert.assertEquals(52, redDot.getNumber());
        Assert.assertEquals(3, redDot.findByPath("a.b").getNumber());
        Assert.assertEquals(34, redDot.findByPath("c.a").getNumber());
        Assert.assertEquals(47, redDot.findByPath("c").getNumber());

        dot.setNumber(0);
        Assert.assertEquals(0, redDot.findByPath("c.a").getNumber());
        Assert.assertEquals(13, redDot.findByPath("c").getNumber());

    }

    @NonNull
    private RedDot init() {
        RedDot redDot = new RedDot("");
        redDot.addChild(
                new RedDot("a").addChildren(
                        new RedDot("a1").addChildren(
                                new RedDot("a-g"),
                                new RedDot("a-h"),
                                new RedDot("a-i"),
                                new RedDot("a-j")
                        ),
                        new RedDot("e"),
                        new RedDot("f")
                )
        );
        redDot.addChild(new RedDot("b").addChild(new RedDot("b1")));
        redDot.addChild(new RedDot("c").addChild(new RedDot("c1")));
        return redDot;
    }

}