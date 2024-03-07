/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package eventsystem;

/**
 *
 * @author gabri
 */
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class User {
    String name;
    String city;
    String email;

    public User(String name, String city, String email) {
        this.name = name;
        this.city = city;
        this.email = email;
    }
}

class Event {
    String name;
    String address;
    String category;
    LocalDateTime dateTime;
    String description;

    public Event(String name, String address, String category, LocalDateTime dateTime, String description) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
        this.description = description;
    }
}

public class EventSystem {
    private static final String EVENTS_FILE = "events.data";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static List<Event> events = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static User currentUser;

    public static void main(String[] args) {
        loadEventsFromFile();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Cadastro de usuário");
            System.out.println("2. Cadastro de evento");
            System.out.println("3. Consultar eventos");
            System.out.println("4. Participar de evento");
            System.out.println("5. Eventos confirmados");
            System.out.println("6. Eventos próximos");
            System.out.println("7. Eventos passados");
            System.out.println("8. Sair");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    registerEvent(scanner);
                    break;
                case 3:
                    displayEvents();
                    break;
                case 4:
                    participateEvent(scanner);
                    break;
                case 5:
                    displayConfirmedEvents();
                    break;
                case 6:
                    displayUpcomingEvents();
                    break;
                case 7:
                    displayPastEvents();
                    break;
                case 8:
                    saveEventsToFile();
                    System.exit(0);
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.println("Cadastro de usuário");
        System.out.print("Nome: ");
        String name = scanner.next();
        System.out.print("Cidade: ");
        String city = scanner.next();
        System.out.print("Email: ");
        String email = scanner.next();

        currentUser = new User(name, city, email);
        users.add(currentUser);

        System.out.println("Usuário cadastrado com sucesso!\n");
    }

    private static void registerEvent(Scanner scanner) {
        if (currentUser == null) {
            System.out.println("Por favor, cadastre-se antes de criar um evento.");
            return;
        }

        System.out.println("Cadastro de evento");
        System.out.print("Nome: ");
        String name = scanner.next();
        System.out.print("Endereço: ");
        String address = scanner.next();
        System.out.print("Categoria (festas, esportes, shows, etc.): ");
        String category = scanner.next();
        System.out.print("Data e Hora (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.next() + " " + scanner.next();
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        System.out.print("Descrição: ");
        String description = scanner.next();

        Event event = new Event(name, address, category, dateTime, description);
        events.add(event);

        System.out.println("Evento cadastrado com sucesso!\n");
    }

    private static void displayEvents() {
        System.out.println("Eventos cadastrados:");
        for (Event event : events) {
            System.out.println(event.name + " - " + event.dateTime.format(DATE_TIME_FORMATTER));
        }
        System.out.println();
    }

    private static void participateEvent(Scanner scanner) {
        displayEvents();
        System.out.print("Digite o nome do evento que deseja participar: ");
        String eventName = scanner.next();

        for (Event event : events) {
            if (event.name.equals(eventName)) {
                System.out.println("Você participou do evento: " + event.name);
                currentUser = null; // Reset current user after participation
                return;
            }
        }

        System.out.println("Evento não encontrado.\n");
    }

    private static void displayConfirmedEvents() {
        if (currentUser == null) {
            System.out.println("Por favor, cadastre-se antes de visualizar eventos confirmados.");
            return;
        }

        System.out.println("Eventos confirmados para " + currentUser.name + ":");
        // Add logic to display confirmed events for the current user
        System.out.println();
    }

    private static void displayUpcomingEvents() {
        System.out.println("Eventos próximos:");
        // Sort events by date/time
        Collections.sort(events, (e1, e2) -> e1.dateTime.compareTo(e2.dateTime));

        for (Event event : events) {
            if (event.dateTime.isAfter(LocalDateTime.now())) {
                System.out.println(event.name + " - " + event.dateTime.format(DATE_TIME_FORMATTER));
            }
        }
        System.out.println();
    }

    private static void displayPastEvents() {
        System.out.println("Eventos passados:");
        // Sort events by date/time
        Collections.sort(events, (e1, e2) -> e1.dateTime.compareTo(e2.dateTime));

        for (Event event : events) {
            if (event.dateTime.isBefore(LocalDateTime.now())) {
                System.out.println(event.name + " - " + event.dateTime.format(DATE_TIME_FORMATTER));
            }
        }
        System.out.println();
    }

    private static void loadEventsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EVENTS_FILE))) {
            events = (List<Event>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Não foi possível carregar os eventos do arquivo. Criando novo arquivo.");
        }
    }

    private static void saveEventsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EVENTS_FILE))) {
            oos.writeObject(events);
        } catch (IOException e) {
            System.out.println("Não foi possível salvar os eventos no arquivo.");
        }
    }
}
