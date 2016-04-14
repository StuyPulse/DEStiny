package edu.stuy.robot.cv.gui;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import edu.stuy.robot.cv.sources.CaptureSource;
import edu.stuy.robot.cv.util.DebugPrinter;

public class ModuleRunner {
    private static ArrayList<CaptureSourceToVisionModuleMapper> sourceDestMap = new ArrayList<CaptureSourceToVisionModuleMapper>();
    private static final int FPS = 10;

    public static void addMapping(CaptureSource captureSource, VisionModule... modules) {
        sourceDestMap.add(new CaptureSourceToVisionModuleMapper(captureSource, modules));
    }

    public void run(Main app) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (CaptureSourceToVisionModuleMapper captureSourceMap : sourceDestMap) {
                        if (captureSourceMap.captureSource.isOpened()) {
                            Mat frame = captureSourceMap.captureSource.read();
                            if (frame == null) {
                                // FIXME: We're reinitializing the capture source so we can loop it when we've reached
                                // the end of the stream. The proper method would be to set the frame pointer for the
                                // source to point back to the beginning of the stream, but this method does not
                                // reliably work.
                                captureSourceMap.captureSource.reinitializeCaptureSource();
                                DebugPrinter.println("Looping capture source");
                            }
                            else {
                                for (VisionModule module : captureSourceMap.modules) {
                                    Thread t = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            long start = System.currentTimeMillis();
                                            module.run(app, frame);
                                            long duration = System.currentTimeMillis() - start;
                                            DebugPrinter.println(module.getName() + ": " + duration + " ms");
                                        }
                                    }, module.getName() + " Thread");
                                    t.setDaemon(true);
                                    t.start();
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000 / FPS);
                    }
                    catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }, "Module Runner Thread");
        t.start();
    }

    public ArrayList<VisionModule> getModules() {
        ArrayList<VisionModule> modules = new ArrayList<VisionModule>();
        for (CaptureSourceToVisionModuleMapper map : sourceDestMap) {
            for (VisionModule module : map.modules) {
                modules.add(module);
            }
        }
        return modules;
    }

    private static class CaptureSourceToVisionModuleMapper {
        private CaptureSource captureSource;
        private VisionModule[] modules;

        public CaptureSourceToVisionModuleMapper(CaptureSource captureSource, VisionModule... modules) {
            this.captureSource = captureSource;
            this.modules = modules;
        }
    }

}
