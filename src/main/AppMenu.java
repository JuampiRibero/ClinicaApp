package main;

import dao.HistoriaClinicaDAO;
import dao.PacienteDAO;
import service.HistoriaClinicaServiceImpl;
import service.PacienteHistoriaService;
import service.PacienteServiceImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppMenu {

    private final Scanner scanner;
    private final MenuHandler menuHandler;
    private boolean running;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        
        
        ServiceBundle services = createServices();
        
        
        this.menuHandler = new MenuHandler(
            this.scanner, 
            services.pacienteService, 
            services.historiaService, 
            services.pacienteHistoriaService
        );
        this.running = true;
    }

    
    public static void main(String[] args) {
        System.out.println("Bienvenido al Sistema de Gestión de Pacientes.");
        AppMenu app = new AppMenu();
        app.run();
    }

    
    public void run() {
        int opcion = 0;
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                opcion = Integer.parseInt(scanner.nextLine());
                processOptionPrincipal(opcion);
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número.");
            }
        }
        scanner.close(); 
    }

    
    private void processOptionPrincipal(int opcion) {
        switch (opcion) {
            case 1:
                runMenuPacientes();
                break;
            case 2:
                runMenuHistorias();
                break;
            case 9:
                System.out.println("Gracias por usar el sistema. ¡Adiós!");
                running = false;
                break;
            default:
                System.out.println("Opción no válida. Por favor, intente de nuevo.");
        }
    }

    
    private void runMenuPacientes() {
        int opcion = 0;
        while (opcion != 9) {
            MenuDisplay.mostrarMenuPacientes();
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                
                
                switch (opcion) {
                    case 1: menuHandler.crearPacienteConHistoria(); break;
                    case 2: menuHandler.leerPacientePorId(); break;
                    case 3: menuHandler.listarPacientes(); break;
                    case 4: menuHandler.actualizarPaciente(); break;
                    case 5: menuHandler.eliminarPaciente(); break;
                    case 6: menuHandler.buscarPacientePorDni(); break;
                    case 9: break; 
                    default: System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número.");
                opcion = 0;
            }
        }
    }

    
    private void runMenuHistorias() {
        int opcion = 0;
        while (opcion != 9) {
            MenuDisplay.mostrarMenuHistorias();
            try {
                opcion = Integer.parseInt(scanner.nextLine());

                
                switch (opcion) {
                    case 1: menuHandler.leerHistoriaPorId(); break;
                    case 2: menuHandler.listarHistorias(); break;
                    case 3: menuHandler.actualizarHistoria(); break;
                    case 9: break; 
                    default: System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número.");
                opcion = 0;
            }
        }
    }

    
    private ServiceBundle createServices() {
        
        HistoriaClinicaDAO historiaDAO = new HistoriaClinicaDAO();
        PacienteDAO pacienteDAO = new PacienteDAO(historiaDAO);
        
        
        PacienteServiceImpl pacienteService = new PacienteServiceImpl(pacienteDAO, historiaDAO);
        HistoriaClinicaServiceImpl historiaService = new HistoriaClinicaServiceImpl(historiaDAO);
        PacienteHistoriaService pacienteHistoriaService = new PacienteHistoriaService(pacienteDAO, historiaDAO);

        
        return new ServiceBundle(pacienteService, historiaService, pacienteHistoriaService);
    }

    
    private record ServiceBundle(
        PacienteServiceImpl pacienteService,
        HistoriaClinicaServiceImpl historiaService,
        PacienteHistoriaService pacienteHistoriaService
    ) {}
}