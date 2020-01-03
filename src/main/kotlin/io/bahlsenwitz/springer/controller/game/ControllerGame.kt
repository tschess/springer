package io.bahlsenwitz.springer.controller.game

import io.bahlsenwitz.springer.controller.game.response.GameActual
import io.bahlsenwitz.springer.controller.game.response.GameHistoric
import io.bahlsenwitz.springer.elo.Outcome
import io.bahlsenwitz.springer.elo.Rating
import io.bahlsenwitz.springer.model.Game
import io.bahlsenwitz.springer.model.Player
import io.bahlsenwitz.springer.notification.PushNotification
import io.bahlsenwitz.springer.repository.RepositoryGame
import io.bahlsenwitz.springer.repository.RepositoryPlayer
import io.bahlsenwitz.springer.request.game.request.*
import io.bahlsenwitz.springer.request.game.update.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors.toList
import javax.validation.Valid

@SpringBootApplication(scanBasePackages = ["io.bahlsenwitz.springer"])
@RestController
@RequestMapping("/game")
class ControllerGame @Autowired
constructor(val repositoryGame: RepositoryGame, val repositoryPlayer: RepositoryPlayer) {

    val threshold: Int = 6

    @PostMapping("/message/seen")
    fun messageSeen(@Valid @RequestBody updateSeen: UpdateSeen): ResponseEntity<Any> {
        val idGame: UUID = UUID.fromString(updateSeen.id_game)!!
        val recordGame = repositoryGame.findById(idGame).get()
        val idPlayer: UUID = UUID.fromString(updateSeen.id_player)!!
        val recordPlayer = repositoryPlayer.findById(idPlayer).get()
        if (recordGame.white.id == recordPlayer.id) {
            recordGame.black_seen = true
            repositoryGame.save(recordGame)
            return ResponseEntity.status(HttpStatus.OK).body("{\"result\":\"success\"}")
        }
        recordGame.white_seen = true
        repositoryGame.save(recordGame)
        return ResponseEntity.status(HttpStatus.OK).body("{\"result\":\"success\"}")
    }

    @PostMapping("/message/request")
    fun messageRequest(@Valid @RequestBody requestMessage: RequestMessage): ResponseEntity<Any> {
        val idGame: UUID = UUID.fromString(requestMessage.id_game)!!
        val game = repositoryGame.findById(idGame).get()
        val whiteMessage = game.white_message
        val whitePosted = game.white_posted
        val blackMessage = game.black_message
        val blackPosted = game.black_posted
        repositoryGame.save(game)
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                "{" +
                        "\"black_message\":\"${blackMessage}\",\"black_seen\":${game.black_seen},\"black_posted\":\"${blackPosted}\"," +
                        "\"white_message\":\"${whiteMessage}\",\"white_seen\":${game.white_seen},\"white_posted\":\"${whitePosted}\"}"
            )
    }

    @PostMapping("/message/update")
    fun messageUpdate(@Valid @RequestBody updateMessage: UpdateMessage): ResponseEntity<Any> {
        val idGame: UUID = UUID.fromString(updateMessage.id_game)!!
        val game = repositoryGame.findById(idGame).get()
        val idPlayer: UUID = UUID.fromString(updateMessage.id_player)!!
        if (game.white.id == idPlayer) {
            game.white_message = updateMessage.message
            game.white_posted = updateMessage.posted
            game.white_seen = false
            repositoryGame.save(game)
            return ResponseEntity.status(HttpStatus.OK).body("{\"result\":\"success\"}")
        }
        game.black_message = updateMessage.message
        game.black_posted = updateMessage.posted
        game.black_seen = false
        repositoryGame.save(game)
        return ResponseEntity.status(HttpStatus.OK).body("{\"result\":\"success\"}")
    }

    @GetMapping("/quick/{self}")
    fun quickPlayer(@PathVariable(value = "self") self: String): ResponseEntity<Any> {
        val selfId: UUID = UUID.fromString(self)!!
        var countOngoing = 0
        val gameListSelf: List<Game> = repositoryGame.getGameListPlayer(selfId)
        gameListSelf.forEach {
            val status = it.status
            if (status == "ONGOING") {
                countOngoing += 1
            }
        }
        if (countOngoing >= threshold) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\":\"ongoing\",\"catalyst\":\"self\"}")
        }
        val thirteenRequest = PageRequest.of(0, 13, Sort.by("elo").descending())
        val thirteenList: List<Player> = repositoryPlayer
            .findAll(thirteenRequest)
            .get()
            .filter { prohibitNewGame(selfId = selfId, opponentId = it.id) }
            .collect(toList())
        if (thirteenList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\":\"availability\",\"catalyst\":\"empty\"}")
        }
        val opponent: Player = thirteenList.shuffled().take(1)[0]
        return ResponseEntity.ok(opponent)
    }

    private fun prohibitNewGame(selfId: UUID, opponentId: UUID): Boolean {
        if (selfId == opponentId) {
            return false
        }
        var countOngoing = 0
        val gameListOpponent: List<Game> = repositoryGame.getGameListPlayer(opponentId)
        gameListOpponent.forEach {
            val status = it.status
            if (status == "ONGOING") {
                countOngoing += 1
            }
        }
        if (countOngoing >= threshold) {
            return false
        }
        val gameListMutual: List<Game> = repositoryGame.getGameListMutual(player0 = selfId, player1 = opponentId)
        gameListMutual.forEach {
            val status = it.status
            if (status == "ONGOING" || status == "PROPOSED") {
                return false
            }
        }
        return true
    }

    @PostMapping("/quick")
    fun quickGame(@Valid @RequestBody requestQuick: RequestQuick): ResponseEntity<Any> {
        val white = repositoryPlayer.getById(UUID.fromString(requestQuick.white_uuid))
        val black = repositoryPlayer.getById(UUID.fromString(requestQuick.black_uuid))
        //TODO: RE-ENABLE NOTIFICATIONS!!!
        PushNotification().execute(
            id = white.id.toString(),
            message = "from ${white.name}",
            title = "new game"
        )
        //TODO: ^^^
        val game = Game(clock = requestQuick.clock, white = white, black = black)
        game.black_update = requestQuick.black_update
        game.updated = requestQuick.updated
        game.created = requestQuick.created
        game.skin = requestQuick.skin
        game.state = requestQuick.state
        game.status = "ONGOING"
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    @PostMapping("/create")
    fun create(@Valid @RequestBody requestCreate: RequestCreate): ResponseEntity<Any> {

        val idSelf: UUID = UUID.fromString(requestCreate.black_uuid)!!
        var countProposedSelf = 0
        val gameListSelf: List<Game> = repositoryGame.getGameListPlayer(idSelf)
        gameListSelf.forEach {
            val status = it.status
            if (status == "PROPOSED") {
                countProposedSelf += 1
            }
        }
        if (countProposedSelf >= threshold) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\":\"proposed\",\"catalyst\":\"self\"}")
        }

        val idOpponent: UUID = UUID.fromString(requestCreate.white_uuid)!!
        var countProposedOpponent = 0
        val gameListOpponent: List<Game> = repositoryGame.getGameListPlayer(idOpponent)
        gameListOpponent.forEach {
            val status = it.status
            if (status == "PROPOSED") {
                countProposedOpponent += 1
            }
        }
        if (countProposedOpponent >= threshold) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\":\"proposed\",\"catalyst\":\"opponent\"}")
        }

        if (idSelf == idOpponent) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\":\"auto\"}")
        }

        val black = repositoryPlayer.getById(idSelf)
        val white = repositoryPlayer.getById(idOpponent)

        val connectionList: List<Game> = repositoryGame.getGameListMutual(player0 = black.id, player1 = white.id)
        connectionList.forEach {
            val status = it.status
            if (status == "ONGOING" || status == "PROPOSED") {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\":\"prior\"}")
            }
        }
        //TODO: RE-ENABLE NOTIFICATIONS!!!
        PushNotification().execute(id = idOpponent.toString(), message = "from ${black.name}", title = "new challenge")
        //TODO: ^^^
        val game = Game(clock = requestCreate.clock, white = white, black = black)
        if (requestCreate.config == "config0") {
            game.config = black.config0
        }
        if (requestCreate.config == "config1") {
            game.config = black.config1
        }
        if (requestCreate.config == "config2") {
            game.config = black.config2
        }
        game.updated = requestCreate.updated
        game.created = requestCreate.created
        ResponseEntity.ok(repositoryGame.save(game))
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\":\"ok\"}")
    }

    /**
     * At some point the pagination of this method should be redundant, envisaging a time
     * when you can only ever have a maximum of 10 active games and 10 open invitations outbound,
     * think about inbound --- anyway. food for thought...
     */
    @PostMapping("/list")
    fun list(@Valid @RequestBody requestPage: RequestPage): ResponseEntity<Any> {
        val gameList: MutableList<Game> = mutableListOf()

        val idSelf = UUID.fromString(requestPage.id)!!
        val gameListSelf: List<Game> = repositoryGame.getGameListPlayer(idSelf)

        gameListSelf.forEach {
            val status = it.status
            if (status == "ONGOING" || status == "PROPOSED") {
                gameList += it
            }
        }
        if (gameList.size <= requestPage.size) {
            val responseList = mutableListOf<GameActual>()
            for (game: Game in gameList) {
                responseList.add(generateGameActual(game = game, id = requestPage.id))
            }
            return ResponseEntity.ok(responseList)
        }
        val brooklyn = ZoneId.of("America/New_York")
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")

        val sortedList =
            gameListSelf.sortedWith(compareBy {
                ZonedDateTime.of(
                    LocalDateTime.parse(it.created, formatter),
                    brooklyn
                )
            })

        val fromIndex: Int = requestPage.page * requestPage.size
        val toIndex: Int = fromIndex + requestPage.size

        val pageList = sortedList.subList(fromIndex, toIndex)

        val responseList = mutableListOf<GameActual>()
        for (game: Game in pageList) {
            responseList.add(generateGameActual(game = game, id = requestPage.id))
        }
        return ResponseEntity.ok(responseList)
    }

    @PostMapping("/historic")
    fun historic(@Valid @RequestBody requestPage: RequestPage): ResponseEntity<Any> {
        val gameList: MutableList<Game> = mutableListOf()

        val idSelf = UUID.fromString(requestPage.id)!!
        val gameListSelf: List<Game> = repositoryGame.getGameListPlayer(idSelf)

        gameListSelf.forEach {
            val status = it.status
            if (status == "RESOLVED") {
                gameList += it
            }
        }

        if (gameList.size <= requestPage.size) {
            val responseList = mutableListOf<GameHistoric>()
            for (game: Game in gameList) {
                responseList.add(generateGameHistoric(game = game, id = requestPage.id))
            }
            return ResponseEntity.ok(responseList)
        }
        val brooklyn = ZoneId.of("America/New_York")
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss.SSSS")

        //val sortedList = gameList.sortedWith(compareBy { ZonedDateTime.of(LocalDateTime.parse(it.created, formatter), brooklyn) })
        gameList.sortByDescending { ZonedDateTime.of(LocalDateTime.parse(it.created, formatter), brooklyn) }

        val fromIndex: Int = requestPage.page * requestPage.size
        val toIndex: Int = fromIndex + requestPage.size

        val pageList = gameList.subList(fromIndex, toIndex)

        val responseList = mutableListOf<GameHistoric>()
        for (game: Game in pageList) {
            responseList.add(generateGameHistoric(game = game, id = requestPage.id))
        }
        return ResponseEntity.ok(responseList)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<Any> {
        val uuid = UUID.fromString(id)
        repositoryGame.deleteById(uuid)
        val result = repositoryGame.existsById(uuid)
        return ResponseEntity.status(HttpStatus.OK).body("{\"result\":${result}}")
    }

    @PostMapping("/accept")
    fun accept(@Valid @RequestBody updateAccept: UpdateAccept): ResponseEntity<Any> {
        val game = repositoryGame.findById(UUID.fromString(updateAccept.id)).get()
        val whiteIapetus = game.white.skin != "NONE"
        val blackIapetus = game.black.skin != "NONE"
        if (whiteIapetus || blackIapetus) {
            game.skin = "IAPETUS"
        }
        game.status = "ONGOING"
        game.updated = updateAccept.updated
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    private fun assignResolution(game: Game, winner: String, catalyst: String, updated: String): Game {
        game.winner = winner
        game.catalyst = catalyst
        game.status = "RESOLVED"
        game.updated = updated
        updateStatistics(nameWinner = winner, white = game.white.name, black = game.black.name)
        return repositoryGame.save(game)
    }

    @PostMapping("/update")
    fun update(@Valid @RequestBody updateGamestate: UpdateGamestate): ResponseEntity<Any> {
        val notificationId = updateGamestate.notification_id
        val notificationMessage = "update from ${updateGamestate.notification_name}"
        val notificationTitle = "your move"
        PushNotification().execute(id = notificationId, message = notificationMessage, title = notificationTitle)

        val game = repositoryGame.findById(UUID.fromString(updateGamestate.id)).get()
        game.turn = updateGamestate.turn
        game.state = updateGamestate.state
        game.highlight = updateGamestate.highlight

        game.white_update = updateGamestate.white_update
        game.black_update = updateGamestate.black_update

        game.catalyst = updateGamestate.catalyst
        game.check_on = updateGamestate.check_on
        game.updated = updateGamestate.updated

        if (updateGamestate.winner == "TBD") {
            repositoryGame.save(game)
            return ResponseEntity.status(HttpStatus.OK).body("{\"update\":\"success\"}")
        }
        if (updateGamestate.winner == "DRAW") {
            this.assignResolution(
                game = game,
                winner = updateGamestate.winner,
                catalyst = updateGamestate.catalyst,
                updated = updateGamestate.updated
            )
            repositoryGame.save(game)
            return ResponseEntity.status(HttpStatus.OK).body("{\"update\":\"success\"}")
        }
        this.assignResolution(
            game = game,
            winner = updateGamestate.winner,
            catalyst = "MATE",
            updated = updateGamestate.updated
        )
        repositoryGame.save(game)
        return ResponseEntity.status(HttpStatus.OK).body("{\"update\":\"success\"}")
    }

    @GetMapping("/polling/{id}")
    fun polling(@PathVariable("id") id: String): ResponseEntity<Game> {
        val game = repositoryGame.findById(UUID.fromString(id)).get()
        return ResponseEntity.ok(game)
    }

    @PostMapping("/test")
    fun test(@Valid @RequestBody requestTest: RequestTest): ResponseEntity<Game> {
        val game = repositoryGame.findById(UUID.fromString("00000000-0000-0000-0000-000000000000")).get()
        if (requestTest.state != null) {
            game.state = requestTest.state
        }
        game.check_on = requestTest.check_on
        game.catalyst = requestTest.catalyst
        game.winner = requestTest.winner
        game.black_update = requestTest.updated
        game.updated = requestTest.updated
        game.created = requestTest.created
        game.status = "ONGOING"
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    @PostMapping("/resign")
    fun resign(@Valid @RequestBody gameUpdateUnilateral: UpdateUnilateral): ResponseEntity<Any> {
        val winner = repositoryPlayer.getById(UUID.fromString(gameUpdateUnilateral.uuid_player))
        val game = repositoryGame.findById(UUID.fromString(gameUpdateUnilateral.uuid_game)).get()
        game.status = "RESOLVED"
        game.winner = winner.name
        game.updated = gameUpdateUnilateral.updated
        updateStatistics(nameWinner = game.winner, white = game.white.name, black = game.black.name)
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    @PostMapping("/draw")
    fun draw(@Valid @RequestBody gameUpdateUnilateral: UpdateUnilateral): ResponseEntity<Any> {
        val player = repositoryPlayer.getById(UUID.fromString(gameUpdateUnilateral.uuid_player))
        val game = repositoryGame.findById(UUID.fromString(gameUpdateUnilateral.uuid_game)).get()
        game.catalyst = player.name
        game.updated = gameUpdateUnilateral.updated
        return ResponseEntity.ok(repositoryGame.save(game))
    }

    private fun updateStatistics(nameWinner: String, white: String, black: String) {
        if (nameWinner == "TBD") {
            return
        }
        if (nameWinner == "DRAW") {
            val player0 = repositoryPlayer.getByName(white)!!
            val rating0 = Rating(rating = player0.elo)

            val player1 = repositoryPlayer.getByName(black)!!
            val rating1 = Rating(rating = player1.elo)

            val elo0 = rating0.update(gameOutcome = Outcome.Draw, opponentRating = player1.elo)
            val elo1 = rating1.update(gameOutcome = Outcome.Draw, opponentRating = player0.elo)

            player0.elo = elo0
            player0.tschx += 2

            player1.elo = elo1
            player1.tschx += 2

            repositoryPlayer.save(player0)
            repositoryPlayer.save(player1)
            return
        }
        val nameLoser = if (white == nameWinner) black else white

        val winner = repositoryPlayer.getByName(nameWinner)!!
        val loser = repositoryPlayer.getByName(nameLoser)!!

        val ratingWinner = Rating(rating = winner.elo)
        val eloWinner = ratingWinner.update(gameOutcome = Outcome.Win, opponentRating = loser.elo)

        val ratingLoser = Rating(rating = loser.elo)
        val eloLoser = ratingLoser.update(gameOutcome = Outcome.Loss, opponentRating = winner.elo)

        winner.elo = eloWinner
        winner.tschx += 3

        loser.elo = eloLoser
        loser.tschx += 1

        repositoryPlayer.save(winner)
        repositoryPlayer.save(loser)
    }

    private fun generateGameActual(game: Game, id: String): GameActual {
        if (id == game.black.id.toString()) {
            return GameActual(
                id = game.id,
                config = game.config,
                status = game.status,
                catalyst = game.catalyst,
                turn = game.turn,
                skin = game.skin,
                created = game.created,
                white_name = game.white.name,
                black_name = game.black.name,
                white_message = game.white_message,
                white_seen = game.white_seen,
                white_posted = game.white_posted,
                black_message = game.black_message,
                black_seen = game.black_seen,
                black_posted = game.black_posted,
                opponent_id = game.white.id,
                opponent_name = game.white.name,
                opponent_avatar = game.white.avatar,
                opponent_elo = game.white.elo,
                opponent_rank = game.white.rank,
                inbound = false
            )
        }
        return GameActual(
            id = game.id,
            config = game.config,
            status = game.status,
            catalyst = game.catalyst,
            turn = game.turn,
            skin = game.skin,
            created = game.created,
            white_name = game.white.name,
            black_name = game.black.name,
            white_message = game.white_message,
            white_seen = game.white_seen,
            white_posted = game.white_posted,
            black_message = game.black_message,
            black_seen = game.black_seen,
            black_posted = game.black_posted,
            opponent_id = game.black.id,
            opponent_name = game.black.name,
            opponent_avatar = game.black.avatar,
            opponent_elo = game.black.elo,
            opponent_rank = game.black.rank,
            inbound = true
        )
    }

    private fun generateGameHistoric(game: Game, id: String): GameHistoric {
        if (id == game.black.id.toString()) {
            return GameHistoric(
                id = game.id,
                white_name = game.white.name,
                black_name = game.black.name,
                state = game.state,
                status = game.status,
                winner = game.winner,
                catalyst = game.catalyst,
                skin = game.skin,
                created = game.created,
                opponent_id = game.white.id,
                opponent_name = game.white.name,
                opponent_avatar = game.white.avatar,
                opponent_elo = game.white.elo,
                opponent_rank = game.white.rank
            )
        }
        return GameHistoric(
            id = game.id,
            white_name = game.white.name,
            black_name = game.black.name,
            state = game.state,
            status = game.status,
            winner = game.winner,
            catalyst = game.catalyst,
            skin = game.skin,
            created = game.created,
            opponent_id = game.black.id,
            opponent_name = game.black.name,
            opponent_avatar = game.black.avatar,
            opponent_elo = game.black.elo,
            opponent_rank = game.black.rank
        )
    }

    @PostMapping("/backup")
    fun backup(): ResponseEntity<Any> {
        val csvHeader = "" +
                "id," +
                "clock," +
                "state," +
                "highlight," +

                "white_id," +
                "white_update," +
                "white_message," +
                "white_posted," +
                "white_seen," +

                "black_id," +
                "black_update," +
                "black_message," +
                "black_posted," +
                "black_seen," +

                "config," +
                "check_on," +
                "turn," +
                "status," +
                "winner," +

                "catalyst," +
                "skin," +
                "updated," +
                "created"

        val gameList = repositoryGame.findAll()

        val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd")
        val timeZone = TimeZone.getTimeZone("America/New_York")
        simpleDateFormat.timeZone = timeZone
        val currentDate = simpleDateFormat.format(Date())

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter("${currentDate}.game.csv")
            fileWriter.append("${csvHeader}\n")
            for (game in gameList) {

                val id: String = game.id.toString()
                fileWriter.append("${id};")
                val clock: String = game.clock.toString()
                fileWriter.append("${clock};")
                val state: String = game.state.toString()
                fileWriter.append("${state};")
                val highlight: String = game.highlight
                fileWriter.append("${highlight};")

                val whiteId: String = game.white.id.toString()
                fileWriter.append("${whiteId};")
                val whiteUpdate: String = game.white_update
                fileWriter.append("${whiteUpdate};")
                //
                val whiteMessage: String = game.white_message
                fileWriter.append("${whiteMessage};")
                val whitePosted: String = game.white_posted
                fileWriter.append("${whitePosted};")
                val whiteSeen: Boolean = game.white_seen
                fileWriter.append("${whiteSeen};")

                val blackId: String = game.black.id.toString()
                fileWriter.append("${blackId};")
                val blackUpdate: String = game.black_update
                fileWriter.append("${blackUpdate};")
                //
                val blackMessage: String = game.black_message
                fileWriter.append("${blackMessage};")
                val blackPosted: String = game.black_posted
                fileWriter.append("${blackPosted};")
                val blackSeen: Boolean = game.black_seen
                fileWriter.append("${blackSeen};")

                val config: String = game.config.toString()
                fileWriter.append("${config};")

                val checkOn: String = game.check_on
                fileWriter.append("${checkOn};")
                val turn: String = game.turn
                fileWriter.append("${turn};")
                val status: String = game.status
                fileWriter.append("${status};")

                val winner: String = game.winner
                fileWriter.append("${winner};")
                val catalyst: String = game.catalyst
                fileWriter.append("${catalyst};")
                val skin: String = game.skin
                fileWriter.append("${skin};")
                val updated: String = game.updated
                fileWriter.append("${updated};")
                val created: String = game.created
                fileWriter.append("${created};")
                fileWriter.append('\n')
            }
        } catch (exception: Exception) {
            println("Writing CSV error!")
            exception.printStackTrace()
        } finally {
            try {
                fileWriter!!.flush()
                fileWriter.close()
            } catch (exception: IOException) {
                println("Flushing/closing error!")
                exception.printStackTrace()
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"backup\":\"game\"}")
    }
}

