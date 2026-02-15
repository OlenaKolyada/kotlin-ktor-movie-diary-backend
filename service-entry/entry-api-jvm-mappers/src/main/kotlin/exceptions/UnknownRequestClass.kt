package com.funkycorgi.vulpecula.entry.api.jvm.mappers.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to EntryContext")
