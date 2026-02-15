package com.funkycorgi.vulpecula.entry.api.jvm.mappers.exceptions

class UnknownRequestClass(requestClass: Class<*>) : RuntimeException("Class $requestClass cannot be mapped to EntryContext")
