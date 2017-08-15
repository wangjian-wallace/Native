package com.wallace.tools.ui.card;

import com.wallace.tools.ui.card.CardContract.Presenter;

/**
 * Created by akmob on 2017/8/15.
 */

 class CardPresenter implements Presenter {
    private CardContract.Card2View view;
    CardPresenter(CardContract.Card2View view){
        this.view = view;
       view.setPresenter(this);
    }

    @Override
    public void getData() {

    }

    @Override
    public void toStart() {

    }

    @Override
    public void toStop() {

    }
}
