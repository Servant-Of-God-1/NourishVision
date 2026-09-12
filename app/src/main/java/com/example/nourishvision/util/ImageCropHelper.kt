package com.example.nourishvision.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.model.AspectRatio
import java.io.File

object ImageCropHelper {
    fun startCrop(
        context: Context,
        sourceUri: Uri,
        launcher: ActivityResultLauncher<Intent>
    ) {
        val destinationUri = Uri.fromFile(
            File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
        )

        val options = UCrop.Options().apply {
            setCompressionFormat(android.graphics.Bitmap.CompressFormat.JPEG)
            setCompressionQuality(90)
            setToolbarColor(android.graphics.Color.parseColor("#2E7D32"))
            setStatusBarColor(android.graphics.Color.parseColor("#1B5E20"))
            setToolbarWidgetColor(android.graphics.Color.WHITE)
            setActiveControlsWidgetColor(android.graphics.Color.parseColor("#2E7D32"))
            setAspectRatioOptions(
                0,
                AspectRatio("1:1", 1f, 1f),
                AspectRatio("3:4", 3f, 4f),
                AspectRatio("4:3", 4f, 3f)
            )
            withAspectRatio(1f, 1f)
        }

        val intent = UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .getIntent(context)

        launcher.launch(intent)
    }

    fun isCropSuccess(resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK
    }

    fun getCroppedUri(context: Context, data: Intent): Uri? {
        return UCrop.getOutput(data)
    }
}