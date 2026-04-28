package com.funkycorgi.vulpecula.entry.cor

data class TestContext(
    var status: CorStatus = CorStatus.NONE,
    var history: String = "",
    var value: Int = 0,
) {
    enum class CorStatus {
        NONE,
        RUNNING,
        FAILING,
        ERROR,
    }
}
