package ru.horev.datastore.models;

import lombok.Getter;
import ru.horev.TicTacToe;
import ru.horev.TicTacToeValue;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@Table(name = "games")
public class Game {
    @Id
    @Column(name = "id")
    private UUID gameId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id_1")
    private Player player1;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id_2")
    private Player player2;

    @Column(name = "winner")
    @Enumerated(EnumType.STRING)
    private TicTacToeValue winner;

    @Transient
    private TicTacToe gameCore;


    public Game() {
    }

    public Game(Player player1) {
        this.gameId = UUID.randomUUID();
        this.player1 = player1;
        this.gameCore = new TicTacToe(3);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId='" + gameId + '\'' +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", winner=" + winner +
                ", gameCore=" + gameCore +
                '}';
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setWinner(TicTacToeValue winner) {
        this.winner = winner;
    }
}