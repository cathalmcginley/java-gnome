/*
 * Widget.java
 *
 * Copyright (c) 2006-2008 Operational Dynamics Consulting Pty Ltd, and Others
 * 
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" plus the "Classpath Exception" (you may link to this code as a
 * library into other programs provided you don't make a derivation of it).
 * See the LICENCE file for the terms governing usage and redistribution.
 */
package org.gnome.gtk;

import org.gnome.gdk.Color;
import org.gnome.gdk.Event;
import org.gnome.gdk.EventButton;
import org.gnome.gdk.EventCrossing;
import org.gnome.gdk.EventExpose;
import org.gnome.gdk.EventFocus;
import org.gnome.gdk.EventKey;
import org.gnome.gdk.EventVisibility;
import org.gnome.gdk.VisibilityState;

/**
 * The base class of all GTK Widgets. Graphical user interface toolkits have
 * long been built up from individual controls and presentation mechanisms
 * that are nested together. These elements are collectively called Widgets.
 * Quite a lot of Widgets contain other Widgets; those are called
 * {@link Container Container}s.
 * 
 * @author Andrew Cowie
 * @author Vreixo Formoso
 * @since 4.0.0
 */
public abstract class Widget extends org.gnome.gtk.Object
{
    protected Widget(long pointer) {
        super(pointer);
    }

    /**
     * Cause this Widget to be activated. This has the same effect as when the
     * user presses the <code>Return</code> key while the Widget is in
     * focus. Calling this method on a {@link ToggleButton} will toggle its
     * state, for example. Whatever signals would normally result from the
     * user activating this Widget by hand will be emitted.
     * 
     * <p>
     * Note that this method only works if this Widget is <var>activatable</var>;
     * not all are, making this more an optional characteristic that some, but
     * not all Widgets share.
     * 
     * <p>
     * The Widget must already be showing on the screen for this method to
     * work (ie, you must invoke {@link #show()} before you can
     * <code>activate()</code> it). Parent Containers must also have been
     * shown.
     * 
     * @throws UnsupportedOperationException
     *             if the Widget is not activatable.
     * @since 4.0.5
     */
    public void activate() {
        if (!GtkWidget.activate(this)) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Cause this Widget to be mapped to the screen. Flags a widget to be
     * displayed. Any widget that isn't shown will not appear on the screen.
     * 
     * <p>
     * There are a bunch of quirks you need to be aware of:
     * 
     * <ul>
     * <li>You have to show the Containers containing a Widget, in addition
     * to the Widget itself, before it will appear on the display.
     * <li>When a toplevel Container is shown (ie, your {@link Window Window}),
     * it is immediately realized and mapped, and any Widgets within it that
     * are shown are then realized and mapped.
     * <li>You can't get information about the actual size that has been
     * allocated to a Widget until it gets mapped to the screen.
     * </ul>
     * 
     * <p>
     * If you want to show all the widgets in a container, it's actually much
     * easier to just call {@link #showAll()} on the container, rather than
     * calling show manually one each individual Widget you've added to it.
     * 
     * @since 4.0.0
     */
    public void show() {
        GtkWidget.show(this);
    }

    /**
     * Cause this Widget, and any Widgets it contains, to be mapped to the
     * screen. You typically call this on a {@link Window Window} after you've
     * finished all the work necessary to set it up.
     * <p>
     * Quite frequently you also want to cause a Window to appear on the
     * screen as well (ie, not be buried under a whole bunch of other
     * applications' Windows), so calling Window's
     * {@link Window#present() present()} is usually next.
     * 
     * <p>
     * <i>Don't be surprised if this takes a few hundred milliseconds.
     * Realizing and mapping all the zillion elements that ultimately make up
     * a Window is one of the most resource intensive operations that GTK,
     * GDK, Pango, your X server, and your kernel have to churn through.
     * Sometimes, you just gotta wait.</i>
     * 
     * @since 4.0.0
     */
    public void showAll() {
        GtkWidget.showAll(this);
    }

    /**
     * Hide the Widget, making it invisible to the user. This can be used to
     * "deactivate" sections of your UI, pending some activity or action that
     * will cause it to be returned to the Window. Note that hiding does not
     * remove it from its parent Container - it just makes the Widget
     * invisible for the time being.
     * 
     * <p>
     * You can call {@link #show()} to make a hidden Widget visible [again].
     * 
     * @since 4.0.4
     */
    public void hide() {
        GtkWidget.hide(this);
    }

    /**
     * Signal emitted when the mouse enters the Widget.
     * 
     * @author Andrew Cowie
     * @author Davyd Madeley
     * @since 4.0.2
     */
    public interface ENTER_NOTIFY_EVENT extends GtkWidget.ENTER_NOTIFY_EVENT
    {
        /**
         * @since 4.0.7
         */
        public boolean onEnterNotifyEvent(Widget source, EventCrossing event);
    }

    /**
     * Hook up a handler to receive <code>ENTER_NOTIFY_EVENT</code> signals
     * on this Widget.
     * 
     * @since 4.0.2
     */
    public void connect(ENTER_NOTIFY_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Signal emitted when the mouse pointer leaves the Widget.
     * 
     * @author Andrew Cowie
     * @since 4.0.7
     */
    public interface LEAVE_NOTIFY_EVENT extends GtkWidget.LEAVE_NOTIFY_EVENT
    {
        boolean onLeaveNotifyEvent(Widget source, EventCrossing event);
    }

    /**
     * Hook up a handler to receive <code>LEAVE_NOTIFY_EVENT</code> signals.
     * 
     * @since 4.0.7
     */
    public void connect(LEAVE_NOTIFY_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Signal emitted when the <i>keyboard</i> focus leaves this Widget.
     * Focus is a concept that is shared evenly between the widget toolkit and
     * the window manager - which often becomes apparent if you're wondering
     * <i>why</i> you have lost focus or regained it.
     * 
     * @author Andrew Cowie
     * @since 4.0.2
     */
    public interface FOCUS_OUT_EVENT extends GtkWidget.FOCUS_OUT_EVENT
    {
        public boolean onFocusOutEvent(Widget source, EventFocus event);
    }

    /**
     * Hook up a handler to receive <code>FOCUS_OUT_EVENT</code> events on
     * this Widget
     * 
     * @since 4.0.2
     */
    public void connect(FOCUS_OUT_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Signal emitted when focus enters this Widget. See
     * {@link Widget.FOCUS_OUT_EVENT FOCUS_OUT_EVENT}.
     * 
     * @author Andrew Cowie
     * @since 4.0.6
     */
    public interface FOCUS_IN_EVENT extends GtkWidget.FOCUS_IN_EVENT
    {
        public boolean onFocusInEvent(Widget source, EventFocus event);
    }

    /**
     * Hook up a handler to receive <code>FOCUS_IN_EVENT</code> signals.
     * 
     * @since 4.0.6
     */
    public void connect(FOCUS_IN_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * The signal emitted when a portion or all of the Widget has been exposed
     * and needs [re]drawing. This can happen when a Widget is first realized
     * to the screen or when part of it has come back from being obscured (by
     * another Window or because it was offscreen).
     * 
     * <p>
     * The <code>event</code> parameter to the callback contains information
     * about the size of the region that was damaged or otherwise needs
     * redrawing. For instance, if you just wanted to know what area was
     * exposed, you could do:
     * 
     * <pre>
     * w.connect(new Widget.EXPOSE_EVENT() {
     *     public boolean onExposeEvent(Widget source, EventExpose event) {
     *         Rectangle rect;
     *         int width, height, x, y;
     *         
     *         rect = event.getArea();
     *         
     *         width = rect.getWidth();
     *         height = rect.getHeight();
     *         x = rect.getX();
     *         y = rect.getY();
     *         
     *         System.out.println(width + &quot;x&quot; + height + &quot; at &quot; + x + &quot;,&quot; + y);
     *     }
     * } 
     * </pre>
     * 
     * <p>
     * The real purpose of <code>EXPOSE_EVENT</code>, however, is to be the
     * the gateway to drawing. GNOME uses the excellent <a
     * href="http://www.cairographics.org/">Cairo 2D graphics library</a> to
     * draw its user interfaces, which we make available in java-gnome in
     * package <code>org.freedesktop.cairo</code>.
     * 
     * <p>
     * Code that does drawing needs to be written a little differently than
     * code which just builds Widgets up into user interfaces. While we are
     * accustomed to doing setting up various Widgets and packing them into
     * Windows in our constructors, the one thing you cannot easily do there
     * is graphics drawing. In order to be able to construct the Context used
     * for drawing operations, Cairo needs the details of the [org.gnome.gdk]
     * Display, Screen and Window it will be drawing to, and these are not
     * available until the Widget has been realized and mapped. The
     * <code>EXPOSE_EVENT</code> signal is emitted exactly at this point,
     * and so that's when we have the environment we need to do our drawing.
     * 
     * <p>
     * To do drawing with Cairo you need a Context. You can instantiate one by
     * asking for the underlying GDK Window backing your Widget and passing it
     * to the Context constructor:
     * 
     * <pre>
     * w.connect(new Widget.EXPOSE_EVENT() {
     *     public boolean onExposeEvent(Widget source, EventExpose event) {
     *         Context cr;
     *         
     *         cr = new Context(source.getWindow());
     *         
     *         // start drawing
     *     }
     * }
     * </pre>
     * 
     * Obviously from here you can carry on using the Cairo Graphics library
     * to do your custom drawing. See
     * {@link org.freedesktop.cairo.Context Context} for further details.
     * 
     * @author Andrew Cowie
     * @since 4.0.7
     */
    /*
     * FIXME when we figure out offscreen drawing, then we can change "cannot
     * easily do".
     */
    public interface EXPOSE_EVENT extends GtkWidget.EXPOSE_EVENT
    {
        /**
         * As with other event signals, the boolean return value indicates
         * whether or not you wish to block further emission of the signal. In
         * general you want to leave the default handlers alone; let them run
         * as well. Return <code>false</code>.
         */
        public boolean onExposeEvent(Widget source, EventExpose event);
    }

    /**
     * Hook up a handler to receive <code>EXPOSE_EVENT</code> signals on
     * this Widget.
     * 
     * @since 4.0.7
     */
    public void connect(EXPOSE_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Handler interface for key press events. While ordinarily the user
     * <i>pressing</i> a key is generally more interesting (in terms of "what
     * key stroke did we get"), it should be noted that by the conventions of
     * modern graphical user interfaces, them <i>releasing</i> a key is when
     * a program should take action if action is called for. For example, if
     * they press and hold the <b><code>Enter</code></b> key while the
     * pointer is over a Button, but then move the mouse off of that Button,
     * subsequently releasing won't cause that Button to activate).
     * 
     * <p>
     * When hooking this up, you'll probably be wanting the key that was hit.
     * That's accomplished with Call <code>event.getKeyval()</code> as in:
     * 
     * <pre>
     * widget.connect(new KEY_PRESS_EVENT() {
     *     public boolean onKeyPressEvent(Widget source, EventKey event) {
     *         final Keyval key;
     *         final ModifierType mod;
     * 
     *         key = event.getKeyval();
     *         mod = event.getState();
     * 
     *         if (key == Keyval.Up) {
     *             // go up!
     *         }
     *         return false;
     *     }
     * });
     * </pre>
     * 
     * but see {@link org.gnome.gdk.Keyval Keyval} for a long discussion of
     * the interpretation and use of keystrokes. Also note that reacting to a
     * key stroke does not imply intercepting it; returning <code>false</code>
     * and letting the default handlers in GTK carry on with things is usually
     * what you want to do.
     * 
     * <p>
     * The release half of this is
     * {@link Widget.KEY_RELEASE_EVENT KEY_RELEASE_EVENT} as you might expect.
     * 
     * @author Andrew Cowie
     * @since 4.0.3
     */
    public interface KEY_PRESS_EVENT extends GtkWidget.KEY_PRESS_EVENT
    {
        /**
         * As with other event signals, returning <code>false</code> means
         * "I didn't [fully] handle this signal, so proceed with the next (ie,
         * usually the default) handler" whereas if you return
         * <code>true</code> you mean "I have handled this event, and wish
         * to stop further emission of the signal".
         */
        public boolean onKeyPressEvent(Widget source, EventKey event);
    }

    /**
     * Hook up a handler to receive <code>KEY_PRESS_EVENT</code> signals on
     * this Widget. For general typing this is the one you want, but for
     * critical events (like pressing <b>Enter</b> to activate a Button that
     * is going to delete things, you might want to postpone action until
     * <code>KEY_RELEASE_EVENT</code>.
     * 
     * @since 4.0.3
     */
    public void connect(KEY_PRESS_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Handler interface for key release events. Calling
     * {@link EventKey#getKeyval() getKeyval()} on the <code>event</code>
     * parameter gets you to the constant representing the key that was
     * actually typed.
     * 
     * @since 4.0.3
     */
    public interface KEY_RELEASE_EVENT extends GtkWidget.KEY_RELEASE_EVENT
    {
        /**
         * (See the comment at
         * {@link Widget.KEY_PRESS_EVENT#onKeyPressEvent(Widget, EventKey) KEY_PRESS_EVENT}
         * to understand the return value)
         */
        public boolean onKeyReleaseEvent(Widget source, EventKey event);
    }

    /**
     * Hook up a handler to receive <code>KEY_RELEASE_EVENT</code> signals
     * on this Widget
     * 
     * @since 4.0.3
     */
    public void connect(KEY_RELEASE_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Return the Container that this Widget is packed into. If the Widget
     * doesn't have a parent, or you're already at a top level Widget (ie, a
     * Window) then expect <code>null</code>.
     * 
     * @since 4.0.2
     */
    public Container getParent() {
        return (Container) getPropertyObject("parent");
    }

    /**
     * Set whether the Widget is greyed out or not. Being insensitive is the
     * term GTK uses for a Widget that is still in its parent layout and still
     * visible on the screen, but no longer responding to user input and
     * de-emphasized on the screen.
     * 
     * <p>
     * This is frequently used on Buttons and MenuItems to show that a given
     * course of action is not currently available, but <i>would</i> be if
     * the user did something different to the application. This can be a
     * terrific hint to assist with discovery, but can be overdone; having
     * everything insensitive and leaving the user no idea what to do next
     * doesn't really help much.
     * 
     * <p>
     * Beware that setting the sensitive property cascades down through the
     * hierarchy of any children packed into this Widget in the same way that
     * {@link #showAll() showAll()} is recursive. While this is usually great
     * for <i>de</i>sensitizing an entire Window, the catch is that if you
     * <i>re</i>sensitize that same Window <b>every</b> Widget within it
     * will return to being sensitive; there's no "memory" of which might have
     * been insensitive before. Thus if you heavily use a mix of sensitive and
     * insensitive states in a Window and desensitize the whole thing while
     * carrying out input validation in a worker thread, you will be left with
     * the task of manually restoring the sensitive state of the various sub
     * components of your UI should you need to return back to that Window to
     * have the user re-enter something.
     * 
     * @param sensitive
     *            <code>true</code> to have the Widget respond as normal,
     *            and <code>false</code> to de-activate the Widget and have
     *            it "grey out".
     * @since 4.0.4
     */
    public void setSensitive(boolean sensitive) {
        GtkWidget.setSensitive(this, sensitive);
    }

    /**
     * Tooltips are notes that will be displayed if a user hovers the mouse
     * pointer over a Widget. They are usually used with controls such as
     * Buttons and Entries to brief the user about that Widget's function.
     * 
     * @param text
     *            The string of plain text (i.e. without any Pango markup) you
     *            wish to be displayed when if the tooltip is popped up.
     * @since 4.0.4
     */
    public void setTooltipText(String text) {
        GtkWidget.setTooltipText(this, text);
    }

    /**
     * Tooltips are notes that will be displayed if a user hovers the mouse
     * pointer over a Widget. They are usually used with controls such as
     * Buttons and Entries to brief the user about that Widget's function.
     * 
     * @param markup
     *            The string with Pango markup you wish to be displayed when
     *            if the tooltip is popped up.
     * @since 4.0.7
     */
    public void setTooltipMarkup(String markup) {
        GtkWidget.setTooltipMarkup(this, markup);
    }

    /**
     * Get the underlying resource being used to power display of, and
     * interaction with, this Widget.
     * 
     * <p>
     * <b>If you're looking for the top Window in a Widget hierarchy, see</b>
     * {@link #getToplevel() getToplevel()}. This method is to get a
     * reference to the lower level GDK mechanisms used by this Widget, not to
     * navigate up a hierarchy of Widgets to find the top-level Window they
     * are packed into.
     * 
     * <p>
     * If what you need are the event handling facilities that go with Widgets
     * that have their own native resources, consider creating an
     * {@link EventBox EventBox} and putting this Widget into it.
     * 
     * <p>
     * <i>If you call this in a class where you're building Windows, then you
     * will probably end up having to use the fully qualified name</i>
     * <code>org.gnome.gdk.Window</code> <i>when declaring variables. That's
     * an unavoidable consequence of the class mapping algorithm we used:
     * <code>GdkWindow</code> is the name of the underlying type being
     * returned, and so Window it is.</i>
     * 
     * @return the <code>org.gnome.gdk.Window</code> associated with this
     *         Widget, or <code>null</code> if this Widget is (as yet)
     *         without one.
     * @since 4.0.4
     */
    public org.gnome.gdk.Window getWindow() {
        return GtkWidgetOverride.getWindow(this);
    }

    /**
     * Adjust the background colour being used when drawing this Widget. This
     * leaves all other style properties unchanged.
     * 
     * <p>
     * If you need to change the background colour behind the text in an Entry
     * or TextView, see
     * {@link #modifyBase(Widget, StateType Color) modifyBase()}.
     * 
     * <p>
     * This is one of a family of "<code>modify</code>" methods; see
     * {@link #modifyStyle(Widget, RcStyle) modifyStyle()} for further details
     * about the interaction of the various theming and style mechanisms.
     * 
     * @since 4.0.5
     */
    public void modifyBackground(StateType state, Color color) {
        GtkWidget.modifyBg(this, state, color);
    }

    /**
     * Set the colour used for text rendered by this Widget. This is the
     * foreground colour; to change the background colour behind text use
     * {@link #modifyBase(StateType, Color) modifyBase()}.
     * 
     * <p>
     * This is one of a family of "<code>modify</code>" methods; see
     * {@link #modifyStyle(Widget, RcStyle) modifyStyle()} for further details
     * about the interaction of the various theming and style mechanisms.
     * 
     * @since 4.0.6
     */
    public void modifyText(StateType state, Color color) {
        GtkWidget.modifyText(this, state, color);
    }

    /**
     * A signal providing notification that the Widget has been obscured or
     * unobscured. To use this, go through the supplied <code>event</code>
     * parameter to get to the VisibilityState as follows:
     * 
     * <pre>
     * w.connect(new Widget.VISIBILITY_NOTIFY_EVENT() {
     *     public boolean onVisibilityNotifyEvent(Widget source, EventVisibility event) {
     *         VisibilityState state = event.getState();
     *         if (state == VisibilityState.FULLY_OBSCURED) {
     *            ...
     *         }
     *     }
     *     return false;
     * });
     * </pre>
     * 
     * See {@link VisibilityState VisibilityState} for the constants
     * describing the possible three possible changes to an underlying
     * element's visibility. See also {@link UNMAP_EVENT UNMAP_EVENT} for a
     * discussion of how this can be used to actively toggle the presentation
     * of a Window to the user.
     * 
     * @author Andrew Cowie
     * @since 4.0.5
     */
    public interface VISIBILITY_NOTIFY_EVENT extends GtkWidget.VISIBILITY_NOTIFY_EVENT
    {
        /**
         * Although this is an <var>event-signal</var>, this merely reports
         * information coming from the underlying X11 windowing system. Since
         * a window being obscured by another application's window is not
         * something you can control (short of requesting the Window holding
         * this Widget always be on-top), it's not entirely clear what good it
         * would do to block further emission of this signal. Return
         * <code>false</code>!
         */
        public boolean onVisibilityNotifyEvent(Widget source, EventVisibility event);
    }

    /**
     * Connect a <code>VISIBILITY_NOTIFY_EVENT</code> handler.
     * 
     * @since 4.0.5
     */
    /*
     * It turns out that two things are necessary for this signal to work: 1)
     * GDK_VISIBILITY_NOTIFY_MASK must be set, and to do that 2) the GDK
     * window must have been assigned. by realize. We do these two steps in
     * the override.
     */
    public void connect(VISIBILITY_NOTIFY_EVENT handler) {
        GtkWidgetOverride.setEventsVisibility(this);
        GtkWidget.connect(this, handler, false);
    }

    /**
     * The signal emitted when a Window becomes invisible. This happens in a
     * variety of scenarios, notably when the Window is minimized, when you
     * change workspaces, and as a Window is being destroyed.
     * 
     * <p>
     * In combination with
     * {@link VISIBILITY_NOTIFY_EVENT VISIBILITY_NOTIFY_EVENT}, this can be
     * used to detect whether a Window is actually currently presented to the
     * top of the stack and visible to the user:
     * 
     * <pre>
     * private boolean up = false;
     * ...
     * final Window w;
     * final Button b;
     * ...
     * w.connect(new Widget.VISIBILITY_NOTIFY_EVENT() {
     *     public boolean onVisibilityNotifyEvent(Widget source, EventVisibility event) {
     *         if (event.getState() == VisibilityState.UNOBSCURED) {
     *             up = true;
     *         } else {
     *             up = false;
     *         }
     *         return false;
     *     }
     * });
     * 
     * w.connect(new Widget.UNMAP_EVENT() {
     *     public boolean onUnmapEvent(Widget source, Event event) {
     *         up = false;
     *         return false;
     *     }
     * });
     * </pre>
     * 
     * thus allowing you to do something like:
     * 
     * <pre>
     * b.connect(new Button.CLICKED() {
     *     public void onClicked(Button source) {
     *         if (up) {
     *             w.hide();
     *             up = false;
     *         } else {
     *             w.present();
     *             up = true;
     *         }
     *     }
     * });
     * </pre>
     * 
     * to intelligently toggle the visibility of the Window.
     * 
     * <p>
     * Note that you don't need <code>MAP_EVENT</code> because the the
     * <code>VISIBILITY_NOTIFY_EVENT</code> will be tripped if you come back
     * to the workspace the Window is already on.
     * 
     * @author Andrew Cowie
     * @author Ryan Lortie
     * @since 4.0.5
     */
    public interface UNMAP_EVENT extends GtkWidget.UNMAP_EVENT
    {
        /**
         * Although this is an <var>event-signal</var>, this merely reports
         * information coming from the underlying X11 windowing system. It's
         * information you can monitor, but don't try to block this signal.
         * Return <code>false</code>!
         */
        boolean onUnmapEvent(Widget source, Event event);
    }

    /**
     * Connect a <code>UNMAP_EVENT</code> handler.
     * 
     * @since 4.0.5
     */
    public void connect(UNMAP_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Does this Widget currently have the keyboard focus?
     * 
     * <p>
     * This can be quite useful when one Widget takes action in a signal
     * handler which changes the state of another Widget. Take for example two
     * related Entry Widgets. The second Entry's <code>CHANGED</code> signal
     * will fire when the first Entry's <code>CHANGED</code> handler calls
     * <code>second.setText()</code>; if it changes the first Entry then
     * you have an infinite loop on your hands. By checking for <var>has-focus</var>
     * at the beginning of both handlers, then only the Widget that the user
     * changed will carry out it's logic; the other will realize it doesn't
     * have focus and can quickly pass.
     * 
     * @since 4.0.6
     */
    public boolean getHasFocus() {
        return getPropertyBoolean("has-focus");
    }

    /**
     * Get the Widget at the top of the container hierarchy to which this
     * Widget belongs.
     * 
     * <p>
     * It's is somewhat common to want to find the ultimately enclosing top
     * level Window that this Widget belongs to. Assuming that the Widget has
     * actually been packed into a Container hierarchy that tops out at a
     * Window (or Dialog, etc) then that is what you'll get. So yes, you can
     * do:
     * 
     * <pre>
     * w = (Window) obj.getToplevel();
     * </pre>
     * 
     * as you'll get a ClassCastException or NullPointerException if you're
     * wrong about <code>obj</code> being in a Window yet.
     * 
     * <p>
     * To manually walk up the hierarchy one level at a time, use
     * {@link #getParent() getParent()}.
     * 
     * @return Will return <code>this</code> if the Widget isn't (yet) in a
     *         hierarchy.
     * @since 4.0.6
     */
    public Widget getToplevel() {
        return GtkWidget.getToplevel(this);
    }

    /**
     * The signal emitted when a Widget is hidden.
     * 
     * @author Andrew Cowie
     * @since 4.0.6
     */
    public interface HIDE extends GtkWidget.HIDE
    {
        public void onHide(Widget source);
    }

    /**
     * Connect a <code>HIDE</code> handler.
     * 
     * @since 4.0.6
     */
    public void connect(Widget.HIDE handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Make this Widget have the keyboard focus for the Window it is within.
     * 
     * <p>
     * Obviously, if this is going to actually have affect, this Widget needs
     * to be <i>in</i> a Window. Furthermore, the Widget needs to be <i>able</i>
     * to take input focus, that is, it must have the <var>can-focus</var>
     * property set (which is inherent to the particular Widget subclass, not
     * something you can change).
     * 
     * @since 4.0.6
     */
    public void grabFocus() {
        GtkWidget.grabFocus(this);
    }

    /**
     * Set the minimum size that will be requested by this Widget of its
     * parent Container.
     * 
     * <p>
     * A major feature of GTK is its adaptability in the face of different
     * languages, fonts, and theme engines, with all of these factors
     * impacting the number of pixels that will be necessary for drawing. The
     * box packing model has each Widget request the size it calculates it
     * needs at runtime of its parent. These requests flow up the Containers
     * the Widget is packed into, with each Container collating the requests
     * from its children. When it reaches the toplevel, GTK negotiates with
     * the X server, and the result is the size allocation for the Window as a
     * whole. The Window proceeds to inform each Container packed into it how
     * much space it has been allocated, leaving it to the Containers to in
     * turn allocate space to each of its children.
     * 
     * <p>
     * The whole point of all this is that in general you are <b>not</b>
     * supposed to interfere with this process. It is virtually impossible to
     * calculate the correct size for a Widget on a given user's desktop ahead
     * of time, so don't try. This method is here for the unusual cases where
     * you need to force a Widget to be a size other than what the default
     * request-allocation process results in.
     * 
     * <p>
     * See {@link Requisition} and {@link Allocation} for further discussion
     * of how the size-request/size-allocation process works and how you can
     * get insight into it.
     * 
     * <p>
     * A value of <code>-1</code> for either <code>width</code> or
     * <code>height</code> will cause that dimension to revert to the
     * "natural" size, that is, the size that would have been requested if
     * you'd left things alone.
     * 
     * <p>
     * Passing <code>0,0</code> is a special case, meaning "as small as
     * possible". This will have varying results and may not actually have
     * much effect.
     * 
     * <p>
     * Incidentally, use
     * {@link Window#setDefaultSize(int, int) setDefaultSize()} for top level
     * Windows, as that method still allows a user to make the Window smaller
     * than the specified default.
     * 
     * @since 4.0.6
     */
    public void setSizeRequest(int width, int height) {
        if ((width < -1) || (height < -1)) {
            throw new IllegalArgumentException("width and height need to be >= -1");
        }
        GtkWidget.setSizeRequest(this, width, height);
    }

    /**
     * Get the details of the rectangle that represents the size that the
     * windowing system down to GTK on down to the parent containers of this
     * Widget have allocated to it. Note that the Widget must already have
     * been realized for the request-allocation cycle to have taken place (ie,
     * the top level Window and all its children must have been
     * <code>show()</code>n. In some circumstances the main loop may need
     * to have iterated).
     * 
     * @since 4.0.6
     */
    public Allocation getAllocation() {
        final Allocation result;

        result = GtkWidgetOverride.getAllocation(this);
        /*
         * We are making a live reference to the GtkAllocation struct member
         * in the GtkWidget class, so we need to make sure that our Allocation
         * Proxy does not survive longer than the Widget. We use this back
         * reference for this purpose.
         */
        result.widget = this;

        return result;
    }

    /**
     * Get the size that will be (is being) requested by this Widget.
     * 
     * <p>
     * In addition to getting the Requisition object for this Widget, this
     * method will also ask the Widget to actually calculate its requirements.
     * This can be a relatively expensive operation as font metrics need to be
     * worked out relative to the Display's physical characteristics (this
     * implies that the request calculation won't have any effect if the
     * Widget is not yet been FIXME to a Screen).
     * 
     * <p>
     * <i>Implementation note: calling this method will invoke
     * <code>gtk_widget_size_request()</code>. The returned Requisition
     * object is "live" however, so once you've got it you can use its getter
     * methods freely without needing to keep calling this method.</i>
     * 
     * @since 4.0.6
     */
    public Requisition getRequisition() {
        final Requisition result;

        result = GtkWidgetOverride.getRequisition(this);
        /*
         * We are making a live reference to the GtkRequisition struct member
         * in the GtkWidget class, so we need to make sure that our
         * Requisition Proxy does not survive longer than the Widget. We use
         * this back reference for this purpose.
         */
        result.widget = this;

        return result;
    }

    /**
     * Signal fired when the user clicks one of their mouse buttons.
     * 
     * <p>
     * Typically, you will use this to do something specific on a mouse click,
     * for example popping up a context menu in response to a "right-click"
     * anywhere in Window <code>w</code>,
     * 
     * <pre>
     * w.connect(new BUTTON_PRESS_EVENT() {
     *     boolean onButtonPressEvent(Widget source, EventButton event) {
     *         if (event.getButton() == MouseButton.RIGHT) {
     *             // popup menu
     *         }
     *         return false;
     *     }
     * });
     * </pre>
     * 
     * <p>
     * Like all event signals, you only return <code>true</code> if you are
     * intercepting this event and want to prevent the default handlers in GTK
     * from running.
     * 
     * <p>
     * The signal emitted when the user lets the button go is
     * {@link Widget.BUTTON_RELEASE_EVENT BUTTON_RELEASE_EVENT}.
     * 
     * <p>
     * Note that this signal doesn't apply just to the user clicking on a
     * Button Widget. Indeed, "left-click" on a Button will cause
     * {@link Button.CLICKED CLICKED} to be emitted, and you should use that
     * in preference for normal purposes.
     * 
     * @author Andrew Cowie
     * @since 4.0.6
     */
    public interface BUTTON_PRESS_EVENT extends GtkWidget.BUTTON_PRESS_EVENT
    {
        public boolean onButtonPressEvent(Widget source, EventButton event);
    }

    /**
     * Hook up a <code>BUTTON_PRESS_EVENT</code> handler.
     * 
     * @since 4.0.6
     */
    /*
     * Do we need to force the GDK event mask like we did with
     * VISIBILITY_NOTIFY_EVENT
     */
    public void connect(Widget.BUTTON_PRESS_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * The signal emitted when the user releases a pressed mouse button. See
     * {@link Widget.BUTTON_PRESS_EVENT BUTTON_PRESS_EVENT} for discussion of
     * this set of event signals.
     * 
     * @author Andrew Cowie
     * @since 4.0.6
     */
    public interface BUTTON_RELEASE_EVENT extends GtkWidget.BUTTON_RELEASE_EVENT
    {
        public boolean onButtonReleaseEvent(Widget source, EventButton event);
    }

    /**
     * Hook up a <code>BUTTON_RELEASE_EVENT</code> handler.
     * 
     * @since 4.0.6
     */
    public void connect(Widget.BUTTON_RELEASE_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }

    /**
     * Whether the Widget can (is willing to) accept the keyboard input focus.
     * There's no default, as such; Widgets able to take keyboard focus will
     * (already) have it <code>true</code>, and others not. The
     * <var>can-focus</var> property is mostly internal, but it is
     * occasionally useful to use a widget that can focus in an environment
     * where you don't want it to take input. Calling
     * <code>setCanFocus(false)</code> will do the trick.
     * 
     * @since 4.0.6
     */
    public void setCanFocus(boolean setting) {
        setPropertyBoolean("can-focus", setting);
    }

    /**
     * Make this Widget attempt to become the default. The default Widget is
     * the one which is activated when the user presses <b><code>Enter</code></b>.
     * 
     * <p>
     * This will only work if the widget is <var>activatable</var>; see
     * {@link #activate() activate()}.
     * 
     * <p>
     * If you're reading this you may in fact be looking for
     * {@link #grabFocus() grabFocus()} which affects where keyboard input is
     * going, as opposed to this method which affects activation.
     * 
     * @since 4.0.8
     */
    public void grabDefault() {
        GtkWidget.grabDefault(this);
    }

    /**
     * The signal emitted when a Widget becomes visible on the screen.
     * 
     * <p>
     * This can be used as an indication that your Window is no longer
     * minimized. Connecting to {@link Widget.EXPOSE_EVENT EXPOSE_EVENT} would
     * probably tell you what you need to know, but if all you want to do is
     * find out your app is [back] onscreen then <code>EXPOSE_EVENT</code>
     * would be a bit heavy handed. Of course, if you are drawing anyway, then
     * it's fine. See {@link Widget.UNMAP_EVENT UNMAP_EVENT} for examples of
     * other variations on the theme of tracking the state of your
     * application.
     * 
     * <p>
     * <i>The interaction between the GTK library we use, its underlying GDK
     * resource management layer, and the the X windowing system which GDK
     * wraps, is complex. Sometimes there is more than one way to do things.</i>
     * 
     * @author Andrew Cowie
     * @since 4.0.8
     */
    public interface MAP_EVENT extends GtkWidget.MAP_EVENT
    {
        /**
         * Although this is an <var>event-signal</var>, this merely reports
         * information coming from the underlying X11 windowing system. It's
         * information you can monitor, but don't try to block this signal.
         * Return <code>false</code>!
         */
        public boolean onMapEvent(Widget source, Event event);
    }

    /**
     * Hook up a <code>MAP_EVENT</code> signal handler.
     * 
     * @since 4.0.8
     */
    public void connect(MAP_EVENT handler) {
        GtkWidget.connect(this, handler, false);
    }
}
