package com.electra.template.core.theme

data class SpacingTokens(
    val xsDp: Int = 4,
    val sDp: Int = 8,
    val mDp: Int = 16,
    val lDp: Int = 24,
    val xlDp: Int = 32,
) {
    companion object {
        val Default = SpacingTokens()
    }
}
