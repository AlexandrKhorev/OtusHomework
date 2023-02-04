package ru.otus.crm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    // Чтобы не было лишних апдейтов в базу, создаем двустороннюю связь Client-Phone.
    // При создании клиента пробегаемся по всем телефонам, переданным в конструктор,
    // и создаем новые объекты телефонов, но уже со ссылкой на клиента
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
