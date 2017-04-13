/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package gwt.material.design.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.AbstractButton;
import gwt.material.design.client.base.HasActive;
import gwt.material.design.client.base.HasProgress;
import gwt.material.design.client.base.mixin.ProgressMixin;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialCollapsible.HasCollapsibleParent;

//@formatter:off

/**
 * Collapsible element to define every items
 *
 * @author kevzlou7979
 * @author Ben Dol
 * @see <a href="http://gwtmaterialdesign.github.io/gwt-material-demo/#collapsible">Material Collapsibles</a>
 * @see <a href="https://material.io/guidelines/components/expansion-panels.html#expansion-panels-behavior">Material Design Specification</a>
 */
//@formatter:on
public class MaterialCollapsibleItem extends AbstractButton implements HasWidgets, HasCollapsibleParent,
        HasProgress, HasActive {

    private MaterialCollapsible parent;
    private MaterialCollapsibleBody body;
    private MaterialCollapsibleHeader header;

    private final ProgressMixin<MaterialCollapsibleItem> progressMixin = new ProgressMixin<>(this);
    private boolean active;

    /**
     * Creates an empty collapsible item.
     */
    public MaterialCollapsibleItem() {
        super();
    }

    @Override
    protected Element createElement() {
        return Document.get().createLIElement();
    }

    /**
     * Adds MaterialCollapsible contents.
     */
    public MaterialCollapsibleItem(final Widget... widgets) {
        this();
        for (Widget w : widgets) {
            add(w);
        }
    }

    @Override
    public void add(Widget child) {
        if (child instanceof MaterialCollapsibleBody) {
            body = (MaterialCollapsibleBody) child;
        } else if (child instanceof MaterialCollapsibleHeader) {
            header = (MaterialCollapsibleHeader) child;
        }
        super.add(child);
    }

    @Override
    public boolean remove(Widget w) {
        if (w instanceof HasCollapsibleParent) {
            ((HasCollapsibleParent) w).setParent(null);
        }

        if (w.equals(body)) {
            body = null;
        } else if (w.equals(header)) {
            header = null;
        }
        return super.remove(w);
    }

    public void setParent(MaterialCollapsible parent) {
        this.parent = parent;

        for (Widget child : this) {
            if (child instanceof HasCollapsibleParent) {
                ((HasCollapsibleParent) child).setParent(parent);
            }
        }
    }

    @Override
    public void setWaves(WavesType waves) {
        super.setWaves(waves);

        // Waves change to inline block.
        // We need to retain 'block' display
        setDisplay(Display.BLOCK);
    }

    /**
     * Expand the body panel.
     */
    public void expand() {
        if (body != null) {
            setActive(true);
            body.setDisplay(Display.BLOCK);
        }
    }

    /**
     * Collapse the cody panel.
     */
    public void collapse() {
        if (body != null) {
            setActive(false);
            body.setDisplay(Display.NONE);
        }
    }

    /**
     * Make this item active.
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
        removeStyleName(CssName.ACTIVE);
        if (header != null) {
            header.removeStyleName(CssName.ACTIVE);
        }
        if (active) {
            if (parent != null) {
                parent.clearActive();
            }
            addStyleName(CssName.ACTIVE);

            if (header != null) {
                header.addStyleName(CssName.ACTIVE);
            }
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void showProgress(ProgressType type) {
        progressMixin.showProgress(type);
    }

    @Override
    public void setPercent(double percent) {
        progressMixin.setPercent(percent);
    }

    @Override
    public void hideProgress() {
        progressMixin.hideProgress();
    }

    public MaterialCollapsibleBody getBody() {
        return body;
    }

    public MaterialCollapsibleHeader getHeader() {
        return header;
    }
}
