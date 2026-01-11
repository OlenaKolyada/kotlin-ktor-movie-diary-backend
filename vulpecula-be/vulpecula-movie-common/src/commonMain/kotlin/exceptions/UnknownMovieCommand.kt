package com.funkycorgi.vulpecula.movie.common.exceptions

import com.funkycorgi.vulpecula.movie.common.models.MovieCommand


class UnknownMovieCommand(command: MovieCommand) : Throwable("Wrong command $command at mapping to Transport stage")
