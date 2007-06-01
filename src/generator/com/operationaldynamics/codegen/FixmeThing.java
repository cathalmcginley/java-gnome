/*
 * FixmeThing.java
 *
 * Copyright (c) 2007 Operational Dynamics Consulting Pty Ltd
 * 
 * The code in this file, and the library it is a part of, are made available
 * to you by the authors under the terms of the "GNU General Public Licence,
 * version 2" See the LICENCE file for the terms governing usage and
 * redistribution.
 */
package com.operationaldynamics.codegen;

/**
 * FIXME! Types that are legit but that we simply don't know what to do with
 * yet, either in the terms of code generator itself or more likely in the
 * architecture of java-gnome as a whole.
 * 
 * @author Andrew Cowie
 */
/*
 * FIXME! In general, these are pointers, so pretend for now. Perhaps the real
 * solution will be to create a new final subclass of Proxy.
 */
public class FixmeThing extends FundamentalThing
{
    public FixmeThing(String gType) {
        super(gType, "long", "long", "jlong");
    }

    protected FixmeThing() {}
}
