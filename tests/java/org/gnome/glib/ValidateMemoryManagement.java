/*
 * ValidateMemoryManagement.java
 *
 * Copyright (c) 2007 Vreixo Formoso Lopes and Others
 * 
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" See the LICENCE file for the terms governing usage and
 * redistribution.
 */
package org.gnome.glib;

import org.gnome.gtk.Button;
import org.gnome.gtk.TestCaseGtk;
import org.gnome.gtk.VBox;

/**
 * Validates memory management by checking many possibly conflicting
 * situations. Note that some situations may exist that are not verified
 * because they cannot be automatically checked from the Java side.
 * 
 * TODO Several tests contemplated here are waiting for a
 * Container.getChildren() implementation.
 * 
 * @author Vreixo Formoso
 */
public class ValidateMemoryManagement extends TestCaseGtk
{

    /**
     * Subclass Button to simplify automatic testing
     */
    private static class MyButtonForTesting extends Button implements Button.CLICKED
    {

        /*
         * we need a static field to check object finalization Of course, this
         * not works if many instances are created!!
         */
        static boolean isFinalized;

        public MyButtonForTesting(String text) {
            super(text);
            isFinalized = false;
        }

        protected void finalize() {
            System.err.println("finalize() called");
            System.err.flush();

            /*
             * I prefer having this commented out, because it helps ensure
             * that we have a robust design against bad user bad practices
             */
            // super.finalize();
            isFinalized = true;
        }

        // ignore
        public void onClicked(Button source) {}

    }

    /**
     * Ensures that java object is removed when only created and put out of
     * scope
     */
    public final void testObjectNeverUsedRemoving() {

        MyButtonForTesting b;

        b = new MyButtonForTesting("Live free, die young!! Like me");

        cycleMainLoop();

        /* object gets out of scope */
        b = null;

        System.gc();
        try {
            /* this is needed because GC runs asynchronously, I think */
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // just ignore
        }

        /* check if button has been deleted */
        assertTrue("An object created but never used is not deleted", MyButtonForTesting.isFinalized);
    }

    /**
     * Ensures that java object is not removed when it is still being used on
     * C side
     */
    public final void testObjectNotRemovedWhenStillGtkAlive() {

        MyButtonForTesting b;
        VBox x;

        x = new VBox(false, 3);
        b = new MyButtonForTesting("Old rockers never die!!");

        /* add button to VBox */
        x.add(b);

        cycleMainLoop();

        /* object gets out of scope */
        b = null;

        System.gc();
        try {
            /* this is needed because GC runs asynchronously, I think */
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // just ignore
        }

        /* check that button hasn't been deleted */
        assertFalse("Object deleted in Java but still alives in Gtk+", MyButtonForTesting.isFinalized);
    }

    /**
     * Ensures that java object is removed when it goes out of scope in both
     * java and C side
     */
    public final void testRemovingAfterNoMoreReachableInBothSides() {

        MyButtonForTesting b;
        VBox x;

        x = new VBox(false, 3);
        b = new MyButtonForTesting("Ready to rock");

        /* add button to VBox */
        x.add(b);

        /* out of scope in Gtk+ */
        x.remove(b);

        /* out of scope in Java */
        b = null;

        System.gc();
        try {
            /* this is needed because GC runs asynchronously, I think */
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // just ignore
        }

        /* check that button has been deleted */
        assertTrue("Object no more needed is not deleted", MyButtonForTesting.isFinalized);
    }

    /**
     * Ensures cyclic references are managed properly
     */
    public final void testTakeCareAboutCyclicReferences() {

        MyButtonForTesting b;
        VBox x;

        x = new VBox(false, 3);
        b = new MyButtonForTesting("I will reference myself");

        /* connect button to itself. cyclic ref! */
        b.connect(b);

        /* add button to VBox */
        x.add(b);

        /* out of scope in Gtk+ */
        x.remove(b);

        /* out of scope in Java */
        b = null;

        System.gc();
        try {
            /* this is needed because GC runs asynchronously, I think */
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // just ignore
        }

        /* check that button has been deleted */
        assertTrue("Implementation don't take care about cyclic refences",
                MyButtonForTesting.isFinalized);
    }

    // TODO to do this we need Container.getChildren implemented :-(
    // public final void testNoDenaturation() { }

    /**
     * Ensures no new Java object is created when user retrieves from Gtk a
     * previously referenced Widget.
     */
    // TODO to do this we need Container.getChildren implemented :-(
    // public final void testNoMultipleCreation() { }
}
