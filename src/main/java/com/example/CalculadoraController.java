package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraController {

    @FXML
    private TextField operacion;

    private boolean resultadoMostrado = false;

    @FXML
    private void numero(ActionEvent event) {

        Button boton = (Button) event.getSource();

        String numero = boton.getText();

        // Si acabamos de obtener un resultado y pulsamos un número,
        // empezamos una nueva operación.
        if (resultadoMostrado) {
            operacion.clear();
            resultadoMostrado = false;
        }

        operacion.appendText(numero);
    }

    @FXML
    private void operacion(ActionEvent event) {

        Button boton = (Button) event.getSource();

        String operador = boton.getText();

        // Si no hay nada escrito, no hacemos nada
        if (operacion.getText().isEmpty()) {
            return;
        }

        // Si acabamos de obtener un resultado,
        // podemos continuar la operación usando ese resultado.
        if (resultadoMostrado) {
            resultadoMostrado = false;
        }

        // Evitamos poner dos operadores seguidos
        String texto = operacion.getText();

        if (texto.endsWith(" + ")
                || texto.endsWith(" - ")
                || texto.endsWith(" * ")
                || texto.endsWith(" / ")) {
            return;
        }

        operacion.appendText(" " + operador + " ");
    }

    @FXML
    private void calcular() {

        String expresion = operacion.getText();

        if (expresion.isEmpty()) {
            return;
        }

        try {

            // Separamos la expresión en números y operadores
            String[] partes = expresion.trim().split(" ");

            List<Double> numeros = new ArrayList<>();
            List<String> operadores = new ArrayList<>();

            // Guardamos números y operadores por separado
            for (int i = 0; i < partes.length; i++) {

                if (i % 2 == 0) {
                    numeros.add(Double.parseDouble(partes[i]));
                } else {
                    operadores.add(partes[i]);
                }
            }

            /*
             * PRIMERO: multiplicaciones y divisiones.
             *
             * Como tienen la misma prioridad,
             * se realizan de izquierda a derecha.
             */
            int i = 0;

            while (i < operadores.size()) {

                String operador = operadores.get(i);

                if (operador.equals("*") || operador.equals("/")) {

                    double izquierda = numeros.get(i);
                    double derecha = numeros.get(i + 1);

                    double resultado;

                    if (operador.equals("*")) {

                        resultado = izquierda * derecha;

                    } else {

                        if (derecha == 0) {
                            operacion.setText("Error");
                            return;
                        }

                        resultado = izquierda / derecha;
                    }

                    // Sustituimos los dos números por el resultado
                    numeros.set(i, resultado);
                    numeros.remove(i + 1);

                    // Eliminamos el operador
                    operadores.remove(i);

                    // Volvemos a comprobar la misma posición
                    // porque puede haber otro * o / después
                } else {
                    i++;
                }
            }

            /*
             * SEGUNDO: sumas y restas.
             *
             * Como tienen la misma prioridad,
             * se realizan de izquierda a derecha.
             */
            i = 0;

            while (i < operadores.size()) {

                String operador = operadores.get(i);

                if (operador.equals("+") || operador.equals("-")) {

                    double izquierda = numeros.get(i);
                    double derecha = numeros.get(i + 1);

                    double resultado;

                    if (operador.equals("+")) {
                        resultado = izquierda + derecha;
                    } else {
                        resultado = izquierda - derecha;
                    }

                    // Sustituimos los dos números por el resultado
                    numeros.set(i, resultado);
                    numeros.remove(i + 1);

                    // Eliminamos el operador
                    operadores.remove(i);

                } else {
                    i++;
                }
            }

            // El único número que queda es el resultado
            operacion.setText(String.valueOf(numeros.get(0)));

            resultadoMostrado = true;

        } catch (Exception e) {

            operacion.setText("Error");
        }
    }

    @FXML
    private void limpiar() {

        operacion.clear();

        resultadoMostrado = false;
    }
}