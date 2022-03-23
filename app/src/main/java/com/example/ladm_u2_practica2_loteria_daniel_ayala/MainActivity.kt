package com.example.ladm_u2_practica2_loteria_daniel_ayala

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ladm_u2_practica2_loteria_daniel_ayala.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var baraja = ArrayList<Carta>()
    var control_juego = true // permite tener el control de detener e inicializar el juego
    var hilo = Hilo(this)
    var nuevo_Juego = false // una vez ejecutado el hilo, solo necesitamos pausar variables y hacer cambios en los botones

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        llenarBaraja() // llenamos baraja con todas la imagenes y audios
        binding.btnInicio.setOnClickListener {
            try {
                if (hilo.verificar == true) {
                    /* Detenemos el hilo, y regresamos a los valores predeterminados del boton*/
                    cancelarVerificacion()
                    return@setOnClickListener
                }
                if (control_juego) {
                    if(!nuevo_Juego) {
                        hilo.start()
                        hilo.pausarHilo()
                        cuentaRegresiva()
                        return@setOnClickListener
                    } else {
                        /* Cuando se pone nuevo_Juego = true ya no ocupamos iniciar el hilo,
                           dado que ya esta pausado, de tal forma que ahora de forma predeterminada se ejecutara el else */
                        hilo.pausarHilo()
                        cuentaRegresiva2()
                        return@setOnClickListener
                    }
                }
                if (!control_juego) {
                    ganador()
                }
            } catch (e:Exception) { }
        }

        binding.btnPausar.setOnClickListener {
            if (hilo.estaPausado() == false) {
                hilo.pausarHilo()
            } else {
                hilo.despausarHilo()
            }
        }

        binding.btnVerificar.setOnClickListener {
            if (hilo.todasCartas()) {
                verificarCartas()
            }
        }

    }

    fun cardMostrar(tam:Int) { // Mostramos las card view
        var vector = ArrayList<Carta>()
        (0..tam-1).forEach {
            vector.add(baraja[it])
        }
        /* Adaptamos las card view para mandarle el vector con las cartas que se van mostrando en pantalla*/
        val adapter = CustomAdapter(vector)
        binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView.scrollToPosition(tam-1)
        binding.recyclerView.adapter = adapter
    }

    fun aleatorio(lista:ArrayList<Carta>) { // Ordena de forma aleatorio el arreglo
        lista.shuffle()
    }

    fun botonesControl() {
        aleatorio(baraja)
        binding.imagen.setImageResource(R.drawable.sevaysecorre)
        binding.btnInicio.setText("Terminar")
        binding.btnInicio.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.purple_700))
        binding.btnPausar.visibility = View.VISIBLE
        binding.btnVerificar.visibility = View.INVISIBLE
        control_juego = false
        binding.btnPausar.setText("PAUSAR")
    }

    fun ganador() {
        hilo.ganador()
        binding.btnInicio.setText("Jugar de nuevo")
        nuevo_Juego = true
        control_juego = true
    }

    fun verificarCartas() {
        hilo.verificar = true
        hilo.despausarHilo()
        binding.btnVerificar.visibility = View.INVISIBLE
        binding.btnInicio.setText("CANCELAR")
        binding.btnPausar.setText("PAUSAR")
    }

    fun cancelarVerificacion() {
        hilo.cancelarHilo()
        hilo.verificar = false
        binding.btnInicio.setText("Jugar de nuevo")
    }

    fun cuentaRegresiva() = GlobalScope.launch {
        var contador = 0
        runOnUiThread {
            binding.imagen.visibility = View.INVISIBLE
            binding.btnInicio.visibility = View.INVISIBLE
            binding.btnVerificar.visibility = View.INVISIBLE
        }
        (contador..2).forEach {
            runOnUiThread {
                binding.txtContador.text = (3-contador+1).toString()
            }
            contador++
            delay(800)
        }
        if (contador == 3) {
            runOnUiThread {
                binding.imagen.visibility = View.VISIBLE
                binding.btnInicio.visibility = View.VISIBLE
                hilo.despausarHilo()
                nuevo_Juego = true
                botonesControl()
            }
        }
    }
    fun cuentaRegresiva2() = GlobalScope.launch {
        var contador = 0
        runOnUiThread {
            binding.imagen.visibility = View.INVISIBLE
            binding.btnInicio.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
            binding.btnVerificar.visibility = View.INVISIBLE
        }
        (contador..2).forEach {
            runOnUiThread {
                binding.txtContador.text = (3-contador+1).toString()
            }
            contador++
            delay(800)
        }
        if (contador == 3) {
            runOnUiThread {
                binding.imagen.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                binding.btnInicio.visibility = View.VISIBLE
                hilo.despausarHilo()
                botonesControl()

                hilo.nuevoJuego()
            }
        }
    }

    fun llenarBaraja() {
        baraja.add(Carta(R.drawable.elcazo,R.raw.elcazoaudio))
        baraja.add(Carta(R.drawable.elpajaro,R.raw.elpajaroaudio))
        baraja.add(Carta(R.drawable.labandera,R.raw.labanderaaudio))
        baraja.add(Carta(R.drawable.elalacran,R.raw.elalacranaudio))
        baraja.add(Carta(R.drawable.elcorazon,R.raw.elcorazonaudio))
        baraja.add(Carta(R.drawable.elparaguas,R.raw.elparaguasaudio))
        baraja.add(Carta(R.drawable.labota,R.raw.labotaaudio))
        baraja.add(Carta(R.drawable.elapache,R.raw.elapacheaudio))
        baraja.add(Carta(R.drawable.elcotorro,R.raw.elcotorroaudio))
        baraja.add(Carta(R.drawable.elpescado,R.raw.elpescadoaudio))
        baraja.add(Carta(R.drawable.labotella,R.raw.labotellaaudio))
        baraja.add(Carta(R.drawable.elarbol,R.raw.elarbolaudio))
        baraja.add(Carta(R.drawable.eldiablito,R.raw.eldiablitoaudio))
        baraja.add(Carta(R.drawable.elpino,R.raw.elpinoaudio))
        baraja.add(Carta(R.drawable.lacalavera,R.raw.lacalaveraaudio))
        baraja.add(Carta(R.drawable.elarpa,R.raw.elarpaaudio))
        baraja.add(Carta(R.drawable.elgallo,R.raw.elgalloaudio))
        baraja.add(Carta(R.drawable.elsol,R.raw.elsolaudio))
        baraja.add(Carta(R.drawable.lacampana,R.raw.lacampanaaudio))
        baraja.add(Carta(R.drawable.elbandolon,R.raw.elbandolonaudio))
        baraja.add(Carta(R.drawable.elgorrito,R.raw.elgorritoaudio))
        baraja.add(Carta(R.drawable.elsoldado,R.raw.elsoldadoaudio))
        baraja.add(Carta(R.drawable.lachalupa,R.raw.lachalupaaudio))
        baraja.add(Carta(R.drawable.elbarril,R.raw.elbarrilaudio))
        baraja.add(Carta(R.drawable.elmelon,R.raw.elmelonaudio))
        baraja.add(Carta(R.drawable.eltambor,R.raw.eltamboraudio))
        baraja.add(Carta(R.drawable.lacorona,R.raw.lacoronaaudio))
        baraja.add(Carta(R.drawable.elborracho,R.raw.elborrachoaudio))
        baraja.add(Carta(R.drawable.elmundo,R.raw.elmundoaudio))
        baraja.add(Carta(R.drawable.elvaliente,R.raw.elvalienteaudio))
        baraja.add(Carta(R.drawable.ladama,R.raw.ladamaaudio))
        baraja.add(Carta(R.drawable.elcamaron,R.raw.elcamaronaudio))
        baraja.add(Carta(R.drawable.elmusico,R.raw.elmusicoaudio))
        baraja.add(Carta(R.drawable.elvenado,R.raw.elvenadoaudio))
        baraja.add(Carta(R.drawable.laescalera,R.raw.laescaleraaudio))
        baraja.add(Carta(R.drawable.elcantarito,R.raw.elcantaritoaudio))
        baraja.add(Carta(R.drawable.elnegrito,R.raw.elnegritoaudio))
        baraja.add(Carta(R.drawable.elvioloncello,R.raw.elvioloncelloaudio))
        baraja.add(Carta(R.drawable.laestrella,R.raw.laestrellaaudio))
        baraja.add(Carta(R.drawable.elcatrin,R.raw.elcatrinaudio))
        baraja.add(Carta(R.drawable.elnopal,R.raw.elnopalaudio))
        baraja.add(Carta(R.drawable.laarana,R.raw.laaranaaudio))
        baraja.add(Carta(R.drawable.lagarza,R.raw.lagarzaaudio))
        baraja.add(Carta(R.drawable.laluna,R.raw.lalunaaudio))
        baraja.add(Carta(R.drawable.lamaceta,R.raw.lamacetaaudio))
        baraja.add(Carta(R.drawable.lamano,R.raw.lamanoaudio))
        baraja.add(Carta(R.drawable.lamuerte,R.raw.lamuerteaudio))
        baraja.add(Carta(R.drawable.lapalma,R.raw.lapalmaaudio))
        baraja.add(Carta(R.drawable.lapera,R.raw.laperaaudio))
        baraja.add(Carta(R.drawable.larana,R.raw.laranaaudio))
        baraja.add(Carta(R.drawable.larosa,R.raw.larosaaudio))
        baraja.add(Carta(R.drawable.lasandia,R.raw.lasandiaaudio))
        baraja.add(Carta(R.drawable.lasirena,R.raw.lasirenaaudio))
        baraja.add(Carta(R.drawable.lasjaras,R.raw.lasjarasaudio))
    }

}