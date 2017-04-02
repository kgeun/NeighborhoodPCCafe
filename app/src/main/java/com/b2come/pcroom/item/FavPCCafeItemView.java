package com.b2come.pcroom.item;

import android.content.Context;

/**
 * Created by KKLee on 2016. 11. 20..
 */

public class FavPCCafeItemView extends PCCafeItemView {
    public FavPCCafeItemView(Context context) {
        super(context);
    }

    public void setFavPCCafeItemData(FavPCCafeItemData favPCCafeItemData) {
        super.setPCCafeItemData(favPCCafeItemData);
    }
}
