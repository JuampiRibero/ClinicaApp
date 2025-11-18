package main;

import models.GrupoSanguineo;
import models.HistoriaClinica;
import models.Paciente;
import service.HistoriaClinicaServiceImpl;
import service.PacienteHistoriaService;
import service.PacienteServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class MenuHandler {

    private final Scanner scanner;
    private final PacienteServiceImpl pacienteService;
    private final HistoriaClinicaServiceImpl historiaService;
    private final PacienteHistoriaService pacienteHistoriaService;

    public MenuHandler(Scanner scanner,
                       PacienteServiceImpl pacienteService, 
                       HistoriaClinicaServiceImpl historiaService, 
                       PacienteHistoriaService pacienteHistoriaService) {
        this.scanner = scanner;
        this.pacienteService = pacienteService;
        this.historiaService = historiaService;
        this.pacienteHistoriaService = pacienteHistoriaService;
    }

    

    public void crearPacienteConHistoria() {
        System.out.println("\n--- Creando Nuevo Paciente y su Historia Clínica ---");
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();
            System.out.print("DNI (sin puntos): ");
            String dni = scanner.nextLine().toUpperCase();
            LocalDate fechaNac = leerFecha("Fecha de Nacimiento (YYYY-MM-DD): ");

            Paciente paciente = new Paciente(null, false, nombre, apellido, dni, fechaNac, null);

            System.out.print("Número de Historia: ");
            String nroHistoria = scanner.nextLine().toUpperCase();
            GrupoSanguineo grupo = leerGrupoSanguineo();
            System.out.print("Antecedentes (opcional): ");
            String antecedentes = scanner.nextLine();
            System.out.print("Medicación Actual (opcional): ");
            String medicacion = scanner.nextLine();
            System.out.print("Observaciones (opcional): ");
            String obs = scanner.nextLine();

            HistoriaClinica historia = new HistoriaClinica(null, false, nroHistoria, grupo, antecedentes, medicacion, obs);

            long nuevoId = pacienteHistoriaService.crearPacienteConHistoria(paciente, historia);
            System.out.println("ÉXITO: Paciente creado con ID " + nuevoId + " y su Historia Clínica asociada.");

        } catch (Exception e) {
            System.err.println("ERROR AL CREAR: " + e.getMessage());
        }
    }

    public void leerPacientePorId() {
        System.out.println("\n--- Leer Paciente por ID ---");
        try {
            System.out.print("Ingrese ID del Paciente: ");
            long id = Long.parseLong(scanner.nextLine());

            Paciente paciente = pacienteService.getById(id);

            if (paciente != null) {
                System.out.println("Paciente encontrado:");
                System.out.println(paciente); 
            } else {
                System.out.println("ERROR: No se encontró Paciente con ID " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.err.println("ERROR AL LEER: " + e.getMessage());
        }
    }

    public void listarPacientes() {
        System.out.println("\n--- Listado de Pacientes Activos ---");
        try {
            List<Paciente> pacientes = pacienteService.getAll();
            if (pacientes.isEmpty()) {
                System.out.println("No hay pacientes registrados para mostrar.");
            } else {
                pacientes.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("ERROR AL LISTAR: " + e.getMessage());
        }
    }

    public void actualizarPaciente() {
        System.out.println("\n--- Actualizar Paciente ---");
        try {
            System.out.print("Ingrese ID del Paciente a actualizar: ");
            long id = Long.parseLong(scanner.nextLine());

            Paciente paciente = pacienteService.getById(id);
            if (paciente == null) {
                System.out.println("ERROR: No se encontró Paciente con ID " + id);
                return;
            }

            System.out.println("Datos actuales: " + paciente);
            System.out.println("Ingrese los nuevos datos (deje en blanco para no cambiar):");
            
            System.out.print("Nombre (" + paciente.getNombre() + "): ");
            String nombre = scanner.nextLine();
            if (!nombre.isBlank()) paciente.setNombre(nombre);

            System.out.print("Apellido (" + paciente.getApellido() + "): ");
            String apellido = scanner.nextLine();
            if (!apellido.isBlank()) paciente.setApellido(apellido);

            System.out.print("DNI (" + paciente.getDni() + "): ");
            String dni = scanner.nextLine().toUpperCase();
            if (!dni.isBlank()) paciente.setDni(dni);

            pacienteService.actualizar(paciente);
            System.out.println("ÉXITO: Paciente ID " + id + " actualizado.");

        } catch (NumberFormatException e) {
            System.err.println("ERROR: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.err.println("ERROR AL ACTUALIZAR: " + e.getMessage());
        }
    }

    public void eliminarPaciente() {
        System.out.println("\n--- Eliminar Paciente (Baja Lógica) ---");
        try {
            System.out.print("Ingrese ID del Paciente a eliminar: ");
            long id = Long.parseLong(scanner.nextLine());

            Paciente paciente = pacienteService.getById(id);
            if (paciente == null) {
                System.out.println("ERROR: No se encontró Paciente con ID " + id);
                return;
            }
            
            System.out.println("Se eliminará (lógicamente) al siguiente paciente y su historia:");
            System.out.println(paciente);
            System.out.print("¿Está seguro? (S/N): ");
            String confirmacion = scanner.nextLine().toUpperCase();

            if (confirmacion.equals("S")) {
                pacienteService.eliminar(id); 
                System.out.println("ÉXITO: Paciente y su Historia Clínica eliminados lógicamente.");
            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (NumberFormatException e) {
            System.err.println("ERROR: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.err.println("ERROR AL ELIMINAR: " + e.getMessage());
        }
    }

    public void buscarPacientePorDni() {
        System.out.println("\n--- Buscar Paciente por DNI ---");
        try {
            System.out.print("Ingrese DNI del Paciente: ");
            String dni = scanner.nextLine().toUpperCase();

            Paciente paciente = pacienteService.getByDni(dni);

            if (paciente != null) {
                System.out.println("Paciente encontrado:");
                System.out.println(paciente);
            } else {
                System.out.println("ERROR: No se encontró Paciente con DNI " + dni);
            }
        } catch (Exception e) {
            System.err.println("ERROR AL BUSCAR: " + e.getMessage());
        }
    }
    
    

    public void leerHistoriaPorId() {
        System.out.println("\n--- Leer Historia Clínica por ID ---");
        try {
            System.out.print("Ingrese ID de la Historia Clínica: ");
            long id = Long.parseLong(scanner.nextLine());

            HistoriaClinica hc = historiaService.getById(id);

            if (hc != null) {
                System.out.println("Historia Clínica encontrada:");
                System.out.println(hc);
            } else {
                System.out.println("ERROR: No se encontró Historia Clínica con ID " + id);
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.err.println("ERROR AL LEER: " + e.getMessage());
        }
    }

    public void listarHistorias() {
        System.out.println("\n--- Listado de Historias Clínicas Activas ---");
        try {
            List<HistoriaClinica> hcs = historiaService.getAll();
            if (hcs.isEmpty()) {
                System.out.println("No hay historias clínicas registradas para mostrar.");
            } else {
                hcs.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.err.println("ERROR AL LISTAR: " + e.getMessage());
        }
    }

    public void actualizarHistoria() {
        System.out.println("\n--- Actualizar Historia Clínica ---");
        try {
            System.out.print("Ingrese ID de la Historia Clínica a actualizar: ");
            long id = Long.parseLong(scanner.nextLine());

            HistoriaClinica hc = historiaService.getById(id);
            if (hc == null) {
                System.out.println("ERROR: No se encontró Historia Clínica con ID " + id);
                return;
            }

            System.out.println("Datos actuales: " + hc);
            System.out.println("Ingrese los nuevos datos (deje en blanco para no cambiar):");

            System.out.print("Antecedentes: ");
            String antecedentes = scanner.nextLine();
            if (!antecedentes.isBlank()) hc.setAntecedentes(antecedentes);

            System.out.print("Medicación Actual: ");
            String medicacion = scanner.nextLine();
            if (!medicacion.isBlank()) hc.setMedicacionActual(medicacion);
            
            System.out.print("Observaciones: ");
            String obs = scanner.nextLine();
            if (!obs.isBlank()) hc.setObservaciones(obs);

            historiaService.actualizar(hc);
            System.out.println("ÉXITO: Historia Clínica actualizada.");

        } catch (NumberFormatException e) {
            System.err.println("ERROR: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.err.println("ERROR AL ACTUALIZAR: " + e.getMessage());
        }
    }

    

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine();
                return LocalDate.parse(input); 
            } catch (DateTimeParseException e) {
                System.err.println("ERROR: Formato de fecha inválido. Debe ser YYYY-MM-DD.");
            }
        }
    }

    private GrupoSanguineo leerGrupoSanguineo() {
        while (true) {
            System.out.println("Seleccione Grupo Sanguíneo:");
            int i = 1;
            for (GrupoSanguineo g : GrupoSanguineo.values()) {
                System.out.println((i++) + ". " + g.toString() + " (" + g.name() + ")");
            }
            System.out.print("Opción (1-" + GrupoSanguineo.values().length + "): ");
            
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                if (opcion >= 1 && opcion <= GrupoSanguineo.values().length) {
                    return GrupoSanguineo.values()[opcion - 1];
                } else {
                    System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.err.println("ERROR: Debe ingresar un NÚMERO entero.");
            }
        }
    }
}