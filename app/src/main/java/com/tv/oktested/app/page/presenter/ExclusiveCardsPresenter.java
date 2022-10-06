/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tv.oktested.app.page.presenter;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.tv.oktested.app.page.entity.DataItem;
import com.tv.oktested.app.page.model.EditorPickModel;
import com.tv.oktested.app.page.presenterView.AllShowCardPresenterView;

import java.util.HashMap;


/**
 * This Presenter will display a card consisting of an image on the left side of the card followed
 * by text on the right side. The image and text have equal width. The text will work like a info
 * box, thus it will be hidden if the parent row is inactive. This behavior is unique to this card
 * and requires a special focus handler.
 */
public class ExclusiveCardsPresenter extends PresenterSelector {
    private final HashMap<DataItem, Presenter> presenters = new HashMap<DataItem, Presenter>();

    @Override
    public Presenter getPresenter(Object item) {

        if (!(item instanceof DataItem)) throw new RuntimeException(
                String.format("The PresenterSelector only supports data items of type '%s'",
                        EditorPickModel.class.getName()));
        DataItem card = (DataItem) item;
        Presenter presenter = presenters.get(card);

        if (presenter == null) {
            presenter = new AllShowCardPresenterView();
            return presenter;
        } else {
            return null;
        }
    }


}
