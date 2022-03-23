package com.example.ladm_u2_practica2_loteria_daniel_ayala

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.view.View
import androidx.core.content.ContextCompat
import java.io.IOException

class Hilo(p:MainActivity) : Thread() {
    var puntero = p
    var contador = -1
    var contador2 = 0
    var verificar = false
    var todas = false
    private var ejecutar = true
    private var pausar = false


    override fun run() {
        super.run()
        while (true) {
            if (ejecutar) {
                if (!pausar) {
                    if (!verificar) {
                        if (contador == puntero.baraja.size - 1) {
                            pausarHilo()
                            terminar()
                            cartasTerminadas()
                        } else {
                            puntero.runOnUiThread {
                                todas = false
                                puntero.cardMostrar(contador)
                                sleep(100)
                                puntero.binding.txtCartas.setText("Cartas: ${contador+1}")
                                puntero.binding.imagen.setImageResource(puntero.baraja[contador].imagen)
                                try {
                                    val mp = MediaPlayer.create(puntero,puntero.baraja[contador].audio)
                                    mp.start()
                                    sleep(1200)
                                    mp.reset()
                                    sleep(100)
                                } catch (e:IOException) {
                                    AlertDialog.Builder(puntero)
                                        .setTitle("FALLO EN EL AUDIO")
                                        .setMessage(e.message)
                                        .setNeutralButton("ACEPTAR", {d,i -> d.dismiss()})
                                        .show()
                                }
                            }
                        }
                    }
                    if (verificar) {
                        if (contador2 == puntero.baraja.size - 1) {
                            pausarHilo()
                            terminar()
                            cartasTerminadas()
                            verificar = false
                            puntero.runOnUiThread {
                                puntero.binding.btnVerificar.visibility = View.INVISIBLE
                            }
                        } else {
                            puntero.runOnUiThread {
                                todas = false
                                puntero.cardMostrar(contador2)
                                puntero.binding.txtCartas.setText("Cartas: ${contador2+1}")
                                puntero.binding.imagen.setImageResource(puntero.baraja[contador2].imagen)
                                try {
                                    val mp = MediaPlayer.create(puntero,puntero.baraja[contador2].audio)
                                    mp.start()
                                    sleep(1200)
                                    mp.reset()
                                    sleep(100)
                                } catch (e:IOException) {
                                    AlertDialog.Builder(puntero)
                                        .setTitle("FALLO EN EL AUDIO")
                                        .setMessage(e.message)
                                        .setNeutralButton("ACEPTAR", {d,i -> d.dismiss()})
                                        .show()
                                }
                            }
                        }
                        contador2++
                    }
                    contador++
                }
            } else {
                contador2 = contador-1
                contador = -1
                puntero.runOnUiThread {
                    puntero.binding.btnInicio.visibility = View.VISIBLE
                }
                ejecutar = true
                todas = true
            }
            sleep(1800L)
        }
    }

    fun pausarHilo() {
        pausar = true
        puntero.runOnUiThread {
            puntero.binding.btnPausar.setText("DESPAUSAR")
        }
    }

    fun despausarHilo() {
        pausar = false
        puntero.runOnUiThread {
            puntero.binding.btnPausar.setText("PAUSAR")
        }
    }

    fun estaPausado(): Boolean {
        return pausar
    }

    fun terminar() {
        ejecutar = false
    }

    fun nuevoJuego() {
        ejecutar = true
        pausar = false
        verificar = false
    }

    fun cartasTerminadas() {
        puntero.runOnUiThread {
            AlertDialog.Builder(puntero)
                .setTitle("CARTAS TERMINADAS")
                .setMessage("Todas las cartas ya fueron reveladas.")
                .setNeutralButton("ACEPTAR", {d,i -> d.dismiss()})
                .show()
            puntero.binding.btnPausar.visibility = View.INVISIBLE // Ocultamos boton para evitar sobrecargar el hilo
            sleep(200)
        }
    }

    fun todasCartas() : Boolean {
        return todas
    }

    fun ganador() {
        terminar()
        pausarHilo()
        puntero.runOnUiThread {
            puntero.binding.btnInicio.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(puntero,R.color.salmon))
            puntero.binding.btnPausar.visibility = View.INVISIBLE
            puntero.binding.btnVerificar.visibility = View.VISIBLE
        }
    }

    fun cancelarHilo() {
        pausarHilo()
        /*contador = -1
        todas = true*/
        terminar()
    }
}