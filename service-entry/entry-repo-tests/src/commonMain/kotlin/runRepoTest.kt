package com.funkycorgi.vulpecula.entry.repo.tests

import kotlinx.coroutines.test.runTest

fun runRepoTest(block: suspend () -> Unit) = runTest { block() }
