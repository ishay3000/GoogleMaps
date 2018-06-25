package com.example.ishaycena.googlemaps.Utilities;

import android.content.Context;
import android.widget.Toast;

public class Utilities implements IUtilities {

    @Override
    public void MakeToast(Context ctx) {
        Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
    }
}
