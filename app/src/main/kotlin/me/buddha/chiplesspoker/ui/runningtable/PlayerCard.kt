package me.buddha.chiplesspoker.ui.runningtable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.buddha.chiplesspoker.domain.model.Hand
import me.buddha.chiplesspoker.domain.model.Player
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.EMPTY
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.FOLDED
import me.buddha.chiplesspoker.domain.utils.PlayingStatus.PLAYING

@Composable
fun PlayerCard(
    player: Player?,  // Null means empty seat
    currentHand: Hand?,
    isCurrentTurn: Boolean,
    onAddPlayer: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(if (isCurrentTurn) 150.dp else 100.dp)
            .background(
                when {
                    player?.playingStatus == PLAYING -> Color.White
                    player?.playingStatus == FOLDED -> Color.Gray.copy(alpha = 0.6f)
                    else -> Color.Gray.copy(alpha = 0.3f)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                2.dp,
                if (isCurrentTurn) Color.Yellow else Color.Black,
                RoundedCornerShape(12.dp)
            )
            .clickable { if (player?.playingStatus == EMPTY) onAddPlayer() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (player?.playingStatus == EMPTY) {
            // Empty Seat - Show Add Player Option
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Player",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp)
                )
                Text("Add", fontSize = 14.sp, color = Color.DarkGray)
            }
        } else {
            // Player Details
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    player?.name ?: "",
                    fontSize = if (isCurrentTurn) 18.sp else 14.sp,
                    fontWeight = FontWeight.Bold
                )


                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "Total: ${player?.chips}",
                    fontSize = if (isCurrentTurn) 16.sp else 12.sp,
                    color = Color.Black
                )
                println("In Pot total = ${currentHand?.currentRound?.playersInvestment?.find { it.playerSeatNo == player?.seatNumber }?.amount}")
                if ((currentHand?.currentRound?.playersInvestment?.find { it.playerSeatNo == player?.seatNumber }?.amount
                        ?: 0L) > 0L
                ) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "Played: ${(currentHand?.currentRound?.playersInvestment?.find { it.playerSeatNo == player?.seatNumber }?.amount ?: 0L)}",
                        fontSize = if (isCurrentTurn) 16.sp else 12.sp,
                        color = Color(0xFF388E3C)
                    )
                }


                if (currentHand?.dealer == player?.seatNumber) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "D",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Red, CircleShape)
                            .padding(4.dp)
                    )
                }
                if (currentHand?.smallBlindPlayer == player?.seatNumber) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "SB",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Blue, CircleShape)
                            .padding(4.dp)
                    )
                }
                if (currentHand?.bigBlindPlayer == player?.seatNumber) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "BB",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Green, CircleShape)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}