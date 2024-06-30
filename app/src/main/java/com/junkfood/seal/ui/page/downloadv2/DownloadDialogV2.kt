package com.junkfood.seal.ui.page.downloadv2

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.UnfoldMore
import androidx.compose.material.icons.outlined.VideoFile
import androidx.compose.material.icons.outlined.VideoSettings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.motion.materialSharedAxisX
import com.junkfood.seal.ui.component.DrawerSheetSubtitle
import com.junkfood.seal.ui.component.FilledButtonWithIcon
import com.junkfood.seal.ui.component.OutlinedButtonWithIcon
import com.junkfood.seal.ui.component.SealModalBottomSheetM2
import com.junkfood.seal.ui.component.SealModalBottomSheetM2Variant
import com.junkfood.seal.ui.component.SingleChoiceSegmentedButton
import com.junkfood.seal.ui.page.download.FormatPagePreview
import com.junkfood.seal.ui.theme.SealTheme
import com.junkfood.seal.util.CUSTOM_COMMAND
import com.junkfood.seal.util.EXTRACT_AUDIO
import com.junkfood.seal.util.PLAYLIST
import com.junkfood.seal.util.PreferenceUtil.updateBoolean
import kotlinx.coroutines.launch

enum class DownloadType {
    Audio, Video, Playlist, Command
}

@Composable
private fun DownloadType.label(): String = stringResource(
    when (this) {
        DownloadType.Audio -> R.string.audio
        DownloadType.Video -> R.string.video
        DownloadType.Command -> R.string.commands
        DownloadType.Playlist -> R.string.playlist
    }
)

private fun DownloadType.updatePreference() {
    when (this) {
        DownloadType.Audio -> {
            EXTRACT_AUDIO.updateBoolean(true)
            CUSTOM_COMMAND.updateBoolean(false)
        }

        DownloadType.Video -> {
            EXTRACT_AUDIO.updateBoolean(false)
            CUSTOM_COMMAND.updateBoolean(false)
        }

        DownloadType.Command -> {
            CUSTOM_COMMAND.updateBoolean(true)
        }

        DownloadType.Playlist -> {
            PLAYLIST.updateBoolean(true)
            CUSTOM_COMMAND.updateBoolean(false)
        }
    }
}

@Composable
private fun ConfigurePage(
    modifier: Modifier = Modifier, onDismissRequest: () -> Unit, onDownload: () -> Unit
) {
    Column(modifier = modifier) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BottomSheet() {
    val scope = rememberCoroutineScope()
    var show by remember { mutableStateOf(0) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    SealTheme {
        SealModalBottomSheetM2(
            sheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Expanded,
                skipHalfExpanded = true
            ),
            sheetGesturesEnabled = false,
            contentPadding = PaddingValues()
        ) {
            AnimatedContent(
                targetState = show,
                label = "",
                transitionSpec = {
                    materialSharedAxisX(
                        initialOffsetX = { it / 4 },
                        targetOffsetX = { -it / 4 })
                }) {
                when (it) {

                    1 -> {
                        Column {
                            Loading()
                            Button(onClick = { scope.launch { sheetState.show() } }) { Text("Show") }
                            Button(onClick = { show = 0 }) { Text("Hide") }
                        }
                    }

                    0 -> {
                        DownloadDialogV2() { show = 1 }
                    }
                }

            }
        }
        SealModalBottomSheetM2Variant(
            sheetState = sheetState,
            sheetGesturesEnabled = false,
            horizontalPadding = PaddingValues(0.dp)
        ) {
            FormatPagePreview()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Loading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(Modifier.height(80.dp))
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
            Text("Fetching formats...", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(Modifier.height(80.dp))
    }


}

@Composable
@Preview
fun DownloadDialogV2(onClick: () -> Unit = {}) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Icon(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            imageVector = Icons.Outlined.DoneAll,
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.settings_before_download),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        var selectedType: DownloadType? by remember { mutableStateOf(DownloadType.Video) }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            DrawerSheetSubtitle(text = stringResource(id = R.string.download_type))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SingleChoiceSegmentedButton(
                    selected = selectedType == DownloadType.Audio,
                    onClick = { selectedType = DownloadType.Audio },
                    shape = SegmentedButtonDefaults.itemShape(0, 3)
                ) {
                    Text(text = stringResource(id = R.string.audio))
                }
                SingleChoiceSegmentedButton(
                    selected = selectedType == DownloadType.Video,
                    onClick = { selectedType = DownloadType.Video },
                    shape = SegmentedButtonDefaults.itemShape(1, 3)
                ) {
                    Text(text = stringResource(id = R.string.video))
                }
                SingleChoiceSegmentedButton(
                    selected = selectedType == DownloadType.Playlist,
                    onClick = { selectedType = DownloadType.Playlist },
                    shape = SegmentedButtonDefaults.itemShape(2, 3)
                ) {
                    Text(text = stringResource(id = R.string.playlist))
                }
            }
        }

        DrawerSheetSubtitle(
            text = stringResource(id = R.string.format_selection),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

//        SingleChoiceItem()
        FormatSelectionAuto()
        FormatSelectionCustom()
        AdditionalSettings()

        Spacer(Modifier.height(12.dp))


        val state = rememberLazyListState()
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.End,
            state = state,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                OutlinedButtonWithIcon(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick = {},
                    icon = Icons.Outlined.Cancel,
                    text = stringResource(R.string.cancel)
                )
            }
            item {
                FilledButtonWithIcon(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClick,
                    icon = Icons.Filled.ArrowForward,
                    text = "Load info",
                    enabled = selectedType != null
                )
            }

        }
    }

}

@Preview
@Composable
private fun AdditionalSettings() {

    Column {
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(thickness = Dp.Hairline, modifier = Modifier.padding(horizontal = 20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Additional settings",
                style = MaterialTheme.typography.labelLarge,
//                    color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

        }
    }

}

@Preview
@Composable
private fun FormatSelectionAuto() {
    MaterialTheme {
        var selected by remember { mutableStateOf(true) }
        SingleChoiceItem(
            title = stringResource(R.string.presets),
            desc = "Prefer Quality, 1080p",
            icon = Icons.Outlined.VideoFile,
            selected = selected,
            action = {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = null, modifier = Modifier.size(18.dp),
                )
            }
        ) {
            selected = !selected
        }
    }
}

@Composable
fun SingleChoiceItem(
    modifier: Modifier = Modifier,
    title: String,
    desc: String,
    icon: ImageVector,
    selected: Boolean,
    action: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(MaterialTheme.shapes.large)
            .background(if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
            .selectable(
                selected = selected,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 32.dp)
            )
        }
        action?.invoke()

    }
}

@Preview
@Composable
private fun FormatSelectionCustom() {
    MaterialTheme {
        var selected by remember { mutableStateOf(false) }
        SingleChoiceItem(
            title = "Custom",
            desc = "Select from formats, subtitles, and customize further",
            icon = Icons.Outlined.VideoSettings,
            selected = selected
        ) {
            selected = !selected
        }
    }
}