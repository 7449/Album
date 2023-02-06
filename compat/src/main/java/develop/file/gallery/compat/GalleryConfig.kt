package develop.file.gallery.compat

object GalleryConfig {

    /*** 首页toolbar返回 result_code*/
    const val RESULT_CODE_TOOLBAR_BACK = -11

    /*** 单选*/
    const val RESULT_CODE_SINGLE_DATA = -12

    /*** 多选*/
    const val RESULT_CODE_MULTIPLE_DATA = -13

    /*** 内置的裁剪所需要的 RESULT_CODE 以及参数*/
    object Crop {
        /*** 裁剪成功之后的 RESULT_CODE*/
        const val RESULT_CODE_CROP = -14
    }

}