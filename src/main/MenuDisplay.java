package main;


public class MenuDisplay {

    public static void mostrarMenuPrincipal() {
        System.out.println("\n---[ SISTEMA DE GESTIÓN DE PACIENTES ]---");
        System.out.println("1. Gestionar Pacientes (A)");
        System.out.println("2. Gestionar Historias Clínicas (B)");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static void mostrarMenuPacientes() {
        System.out.println("\n---[ Gestión de Pacientes (A) ]---");
        System.out.println("1. Crear Nuevo Paciente (con Historia Clínica)");
        System.out.println("2. Leer Paciente por ID");
        System.out.println("3. Listar todos los Pacientes");
        System.out.println("4. Actualizar datos de Paciente");
        System.out.println("5. Eliminar Paciente (Baja Lógica)");
        System.out.println("6. Buscar Paciente por DNI");
        System.out.println("9. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    public static void mostrarMenuHistorias() {
        System.out.println("\n---[ Gestión de Historias Clínicas (B) ]---");
        System.out.println("NOTA: La creación se realiza automáticamente al crear un Paciente.");
        System.out.println("1. Leer Historia Clínica por ID");
        System.out.println("2. Listar todas las Historias Clínicas");
        System.out.println("3. Actualizar Historia Clínica");
        System.out.println("9. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }
}