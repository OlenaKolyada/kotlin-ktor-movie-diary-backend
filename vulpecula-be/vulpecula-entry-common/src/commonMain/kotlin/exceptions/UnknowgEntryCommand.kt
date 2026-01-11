package com.funkycorgi.vulpecula.entry.common.exceptions

import com.funkycorgi.vulpecula.entry.common.models.EntryCommand


class UnknowgEntryCommand(command: EntryCommand) : Throwable("Wrong command $command at mapping to Transport stage")
