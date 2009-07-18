/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, IconValue.java is part of JTStand.
 *
 * JTStand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JTStand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jdesktop.swingx.renderer;

import java.io.Serializable;

import javax.swing.Icon;

import org.jdesktop.swingx.icon.EmptyIcon;

/**
 * A simple converter to return a Icon representation of an Object.
 * <p>
 * 
 * This class is intended to be the "small coin" to configure/format icon cell
 * content of concrete subclasses of <code>ComponentProvider</code>.
 * <p>
 * 
 * 
 * NOTE: this is experimental, most probably will change. A (near) future
 * version with change the signature of the getIcon method to
 * 
 * <pre><code>
 * Icon getIcon(Object value, IconType type);
 * </code></pre>
 * 
 * That will allow a more fine-grained control of custom icons in tree rendering.
 * 
 * @author Jeanette Winzenburg
 */
public interface IconValue extends Serializable {
    
    /**
     * The cell type the icon is used for.
     */
    public enum IconType {
        
        LEAF,
        
        OPEN_FOLDER,
        
        CLOSED_FOLDER
        
    }
    
    /**
     * A marker icon used to indicate a null. 
     * 
     */
    public final static Icon NULL_ICON = new EmptyIcon();
    
    
    /**
     * Returns a icon representation of the given value.
     * 
     * @param value the object to present as Icon
     * @return a Icon representation of the given value, 
     *  may be null if none available.
     *  
     */
    Icon getIcon(Object value);

}
