package com.code42.archive;

import java.util.List;

import com.backup42.common.perm.C42PermissionPro;
import com.code42.archive.ArchiveDtoQueryBase.ArchiveDtoQueryBuilder;
import com.code42.archive.ArchiveDtoQueryBase.OrderBy;
import com.code42.archive.ArchiveDtoQueryBase.OrderDir;
import com.code42.core.CommandException;
import com.code42.core.auth.impl.CoreSession;
import com.code42.core.impl.DBCmd;
import com.code42.utils.Pair;

public class ArchiveDtoFindByDestinationCmd extends DBCmd<Pair<List<ArchiveDto>, Integer>> {

	private final int destinationId;
	private final Integer limit;
	private final Integer offset;
	private final boolean exportAll;
	private final OrderBy orderBy;
	private final OrderDir orderDir;

	public ArchiveDtoFindByDestinationCmd(int destinationId, Integer limit, Integer offset) {
		this(destinationId, limit, offset, false, null, null);
	}

	public ArchiveDtoFindByDestinationCmd(int destinationId, Integer limit, Integer offset, boolean exportAll,
			OrderBy orderBy, OrderDir orderDir) {
		super();
		this.destinationId = destinationId;
		this.limit = limit;
		this.offset = offset;
		this.exportAll = exportAll;
		this.orderBy = orderBy;
		this.orderDir = orderDir;
	}

	@Override
	public Pair<List<ArchiveDto>, Integer> exec(CoreSession session) throws CommandException {
		this.auth.isAuthorized(session, C42PermissionPro.System.SYSTEM_SETTINGS);

		final ArchiveDtoQueryBuilder b = new ArchiveDtoQueryBase.ArchiveDtoQueryBuilder().destination(this.destinationId)
				.limit(this.limit).offset(this.offset).exportAll(this.exportAll);
		if (this.orderBy != null && this.orderDir != null) {
			b.orderBy(this.orderBy);
			b.orderDir(this.orderDir);
		}
		List<ArchiveDto> list = this.db.find(b.buildSelect());
		Integer count = this.db.find(b.buildCount());
		return new Pair(list, count);
	}
}
