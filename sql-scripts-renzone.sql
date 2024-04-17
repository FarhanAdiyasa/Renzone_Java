USE RZ2
/****** Object:  Table [dbo].[dtl_game]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[dtl_game](
	[id_game] [varchar](10) NOT NULL,
	[id_kategori] [varchar](10) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_game] ASC,
	[id_kategori] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ms_game]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ms_game](
	[id_game] [varchar](10) NOT NULL,
	[nama_game] [varchar](30) NOT NULL,
	[deskripsi_game] [varchar](30) NOT NULL,
	[tanggal_rilis] [date] NOT NULL,
	[jenis_ps] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_game] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ms_kategorigame]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ms_kategorigame](
	[id_kategori] [varchar](10) NOT NULL,
	[nama_kategori] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_kategori] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

/****** Object:  Table [dbo].[trs_peminjaman]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[trs_peminjaman](
	[id_peminjaman] [varchar](20) NOT NULL,
	[tanggal_peminjaman] [date] NOT NULL,
	[tanggal_pengembalian] [date] NOT NULL,
	[biaya] [money] NOT NULL,
	[id_member] [varchar](10) NULL,
	[id_karyawan] [varchar](10) NULL,
	[bayar] [money] NOT NULL,
	[foto_ktp] [image] NULL,
 CONSTRAINT [PK__trs_pemi__546CC69E53B125A4] PRIMARY KEY CLUSTERED 
(
	[id_peminjaman] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[trs_paket]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[trs_paket](
	[id_pembelian] [varchar](20) NOT NULL,
	[tanggal_pembelian] [varchar](20) NOT NULL,
	[id_karyawan] [varchar](10) NULL,
	[id_member] [varchar](10) NULL,
	[nama_pembeli] [varchar](50) NOT NULL,
	[id_meja] [varchar](10) NULL,
	[total_transaksi] [money] NOT NULL,
	[jumlah_bayar] [money] NOT NULL,
	[tanggal_selesai] [varchar](20) NOT NULL,
 CONSTRAINT [PK__trs_pake__8EF0D667AD8C95DD] PRIMARY KEY CLUSTERED 
(
	[id_pembelian] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[trs_pengembalian]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[trs_pengembalian](
	[id_pengembalian] [varchar](20) NOT NULL,
	[tanggal_pengembalian] [date] NOT NULL,
	[denda] [money] NOT NULL,
	[id_karyawan] [varchar](10) NULL,
	[bayar] [money] NOT NULL,
	[id_peminjaman] [varchar](20) NOT NULL,
 CONSTRAINT [PK__trs_peng__9E1B8DAE76FC4C0C] PRIMARY KEY CLUSTERED 
(
	[id_pengembalian] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[trs_member]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[trs_member](
	[id_trsmember] [varchar](20) NOT NULL,
	[id_member] [varchar](10) NULL,
	[total_biaya] [money] NOT NULL,
	[tanggal_mulai] [date] NOT NULL,
	[tanggal_akhir] [date] NOT NULL,
	[id_karyawan] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[id_trsmember] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[transaksi_view]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ms_ps](
	[id_ps] [varchar](10) NOT NULL,
	[no_seri] [varchar](30) NOT NULL,
	[deskripsi] [varchar](30) NOT NULL,
	[harga_sewa] [money] NOT NULL,
	[jenis_ps] [varchar](30) NOT NULL,
	[kondisi_ps] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_ps] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ms_aksesoris]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ms_aksesoris](
	[id_aksesoris] [varchar](10) NOT NULL,
	[nama_aksesoris] [varchar](30) NOT NULL,
	[harga_sewa] [money] NOT NULL,
	[jenis_aksesoris] [varchar](30) NOT NULL,
	[kondisi_aksesoris] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_aksesoris] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[dtl_pinjamaksesoris](
	[id_peminjaman] [varchar](20) NOT NULL,
	[id_aksesoris] [varchar](10) NOT NULL,
	[kondisi_pengembalian] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[id_peminjaman] ASC,
	[id_aksesoris] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[dtl_peminjamanps](
	[id_peminjaman] [varchar](20) NOT NULL,
	[id_ps] [varchar](10) NOT NULL,
	[kondisi_pengembalian] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[id_peminjaman] ASC,
	[id_ps] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[bntu_member](
	[id_member] [varchar](10) NOT NULL,
	[nama_member] [varchar](30) NOT NULL,
	[alamat] [varchar](30) NOT NULL,
	[no_telp] [varchar](13) NOT NULL,
	[tgl_daftar] [date] NOT NULL,
	[status_anggota] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_member] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[dtl_layanan]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[dtl_layanan](
	[id_paket] [varchar](10) NOT NULL,
	[id_pembelian] [varchar](20) NOT NULL,
	[quantity] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_paket] ASC,
	[id_pembelian] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[dtl_ps]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[dtl_ps](
	[id_game] [varchar](10) NOT NULL,
	[id_ps] [varchar](10) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_game] ASC,
	[id_ps] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[increment_laporan]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[increment_laporan](
	[no_income] [varchar](50) NULL,
	[no_asset] [varchar](50) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[login]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[login](
	[id_karyawan] [varchar](10) NULL,
	[nama_karyawan] [varchar](30) NULL,
	[foto_profil] [image] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ms_karyawan]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ms_karyawan](
	[id_karyawan] [varchar](10) NOT NULL,
	[nama_karyawan] [varchar](30) NOT NULL,
	[alamat] [varchar](30) NOT NULL,
	[no_telp] [varchar](13) NOT NULL,
	[foto_profil] [image] NULL,
	[nama_pengguna] [varchar](30) NOT NULL,
	[kata_sandi] [varchar](30) NOT NULL,
	[isDeleted] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[id_karyawan] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ms_meja]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ms_meja](
	[id_meja] [varchar](10) NOT NULL,
	[id_ps] [varchar](10) NULL,
	[merkTV] [varchar](30) NOT NULL,
	[deskripsi] [varchar](100) NOT NULL,
	[isDeleted] [varchar](10) NULL,
 CONSTRAINT [PK__ms_meja__68A1B86CC273631A] PRIMARY KEY CLUSTERED 
(
	[id_meja] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ms_member]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ms_member](
	[id_member] [varchar](10) NOT NULL,
	[nama_member] [varchar](30) NOT NULL,
	[alamat] [varchar](30) NOT NULL,
	[no_telp] [varchar](13) NOT NULL,
	[tgl_daftar] [date] NOT NULL,
	[status_anggota] [varchar](30) NOT NULL,
	[isDeleted] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[id_member] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ms_paketlayanan]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ms_paketlayanan](
	[id_paket] [varchar](10) NOT NULL,
	[nama_paket] [varchar](30) NOT NULL,
	[tipe_paket] [varchar](30) NOT NULL,
	[deskripsi] [varchar](30) NOT NULL,
	[durasi] [int] NOT NULL,
	[biaya] [money] NOT NULL,
	[jenis_ps] [varchar](30) NOT NULL,
	[isDeleted] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[id_paket] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[temp_Img]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[temp_Img](
	[ktp] [image] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO

CREATE NONCLUSTERED INDEX [nci_filterfortrs_member] ON [dbo].[ms_member]
(
	[id_member] ASC,
	[nama_member] ASC,
	[status_anggota] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[bntu_member] ADD  DEFAULT ('DELETED') FOR [id_member]
GO
ALTER TABLE [dbo].[dtl_layanan] ADD  DEFAULT ('DELETED') FOR [id_paket]
GO
ALTER TABLE [dbo].[dtl_peminjamanps] ADD  DEFAULT ('DELETED') FOR [id_ps]
GO
ALTER TABLE [dbo].[dtl_pinjamaksesoris] ADD  DEFAULT ('DELETED') FOR [id_aksesoris]
GO
ALTER TABLE [dbo].[dtl_ps] ADD  DEFAULT ('DELETED') FOR [id_game]
GO
ALTER TABLE [dbo].[ms_aksesoris] ADD  DEFAULT ('DELETED') FOR [id_aksesoris]
GO
ALTER TABLE [dbo].[ms_game] ADD  DEFAULT ('DELETED') FOR [id_game]
GO
ALTER TABLE [dbo].[ms_karyawan] ADD  DEFAULT ('DELETED') FOR [id_karyawan]
GO
ALTER TABLE [dbo].[ms_kategorigame] ADD  DEFAULT ('DELETED') FOR [id_kategori]
GO
ALTER TABLE [dbo].[ms_meja] ADD  CONSTRAINT [DF__ms_meja__id_meja__48CFD27E]  DEFAULT ('DELETED') FOR [id_meja]
GO
ALTER TABLE [dbo].[ms_member] ADD  DEFAULT ('DELETED') FOR [id_member]
GO
ALTER TABLE [dbo].[ms_member] ADD  CONSTRAINT [DF_ms_member_nama_member]  DEFAULT ('DELETED') FOR [nama_member]
GO
ALTER TABLE [dbo].[ms_paketlayanan] ADD  DEFAULT ('DELETED') FOR [id_paket]
GO
ALTER TABLE [dbo].[ms_ps] ADD  DEFAULT ('DELETED') FOR [id_ps]
GO
ALTER TABLE [dbo].[trs_member] ADD  DEFAULT ('DELETED') FOR [id_trsmember]
GO
ALTER TABLE [dbo].[trs_member] ADD  DEFAULT ('DELETED') FOR [id_member]
GO
ALTER TABLE [dbo].[trs_member] ADD  DEFAULT ('DELETED') FOR [id_karyawan]
GO
ALTER TABLE [dbo].[trs_paket] ADD  CONSTRAINT [DF__trs_paket__id_pe__5629CD9C]  DEFAULT ('DELETED') FOR [id_pembelian]
GO
ALTER TABLE [dbo].[trs_paket] ADD  CONSTRAINT [DF__trs_paket__id_ka__571DF1D5]  DEFAULT ('DELETED') FOR [id_karyawan]
GO
ALTER TABLE [dbo].[trs_paket] ADD  CONSTRAINT [DF__trs_paket__id_me__5812160E]  DEFAULT ('DELETED') FOR [id_member]
GO
ALTER TABLE [dbo].[trs_paket] ADD  CONSTRAINT [DF__trs_paket__id_me__59063A47]  DEFAULT ('DELETED') FOR [id_meja]
GO
ALTER TABLE [dbo].[trs_peminjaman] ADD  CONSTRAINT [DF__trs_pemin__id_pe__4F7CD00D]  DEFAULT ('DELETED') FOR [id_peminjaman]
GO
ALTER TABLE [dbo].[trs_peminjaman] ADD  CONSTRAINT [DF__trs_pemin__id_me__5070F446]  DEFAULT ('DELETED') FOR [id_member]
GO
ALTER TABLE [dbo].[trs_peminjaman] ADD  CONSTRAINT [DF__trs_pemin__id_ka__5165187F]  DEFAULT ('DELETED') FOR [id_karyawan]
GO
ALTER TABLE [dbo].[trs_pengembalian] ADD  CONSTRAINT [DF__trs_penge__id_pe__5EBF139D]  DEFAULT ('DELETED') FOR [id_pengembalian]
GO
ALTER TABLE [dbo].[trs_pengembalian] ADD  CONSTRAINT [DF__trs_penge__id_ka__5FB337D6]  DEFAULT ('DELETED') FOR [id_karyawan]
GO
ALTER TABLE [dbo].[dtl_game]  WITH CHECK ADD FOREIGN KEY([id_game])
REFERENCES [dbo].[ms_game] ([id_game])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[dtl_game]  WITH CHECK ADD FOREIGN KEY([id_kategori])
REFERENCES [dbo].[ms_kategorigame] ([id_kategori])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[dtl_layanan]  WITH CHECK ADD  CONSTRAINT [FK__dtl_layan__id_pa__6E01572D] FOREIGN KEY([id_paket])
REFERENCES [dbo].[ms_paketlayanan] ([id_paket])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[dtl_layanan] CHECK CONSTRAINT [FK__dtl_layan__id_pa__6E01572D]
GO
ALTER TABLE [dbo].[dtl_layanan]  WITH CHECK ADD  CONSTRAINT [FK__dtl_layan__id_pe__6EF57B66] FOREIGN KEY([id_pembelian])
REFERENCES [dbo].[trs_paket] ([id_pembelian])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[dtl_layanan] CHECK CONSTRAINT [FK__dtl_layan__id_pe__6EF57B66]
GO
ALTER TABLE [dbo].[dtl_peminjamanps]  WITH CHECK ADD  CONSTRAINT [FK__dtl_pemin__id_pe__7A672E12] FOREIGN KEY([id_peminjaman])
REFERENCES [dbo].[trs_peminjaman] ([id_peminjaman])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[dtl_peminjamanps] CHECK CONSTRAINT [FK__dtl_pemin__id_pe__7A672E12]
GO
ALTER TABLE [dbo].[dtl_peminjamanps]  WITH CHECK ADD FOREIGN KEY([id_ps])
REFERENCES [dbo].[ms_ps] ([id_ps])
ON UPDATE CASCADE
ON DELETE SET DEFAULT
GO
ALTER TABLE [dbo].[dtl_pinjamaksesoris]  WITH CHECK ADD  CONSTRAINT [FK__dtl_pinja__id_ak__08B54D69] FOREIGN KEY([id_aksesoris])
REFERENCES [dbo].[ms_aksesoris] ([id_aksesoris])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[dtl_pinjamaksesoris] CHECK CONSTRAINT [FK__dtl_pinja__id_ak__08B54D69]
GO
ALTER TABLE [dbo].[dtl_pinjamaksesoris]  WITH CHECK ADD  CONSTRAINT [FK__dtl_pinja__id_pe__07C12930] FOREIGN KEY([id_peminjaman])
REFERENCES [dbo].[trs_peminjaman] ([id_peminjaman])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[dtl_pinjamaksesoris] CHECK CONSTRAINT [FK__dtl_pinja__id_pe__07C12930]
GO
ALTER TABLE [dbo].[dtl_ps]  WITH CHECK ADD  CONSTRAINT [FK__dtl_ps__id_game__7F2BE32F] FOREIGN KEY([id_game])
REFERENCES [dbo].[ms_game] ([id_game])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[dtl_ps] CHECK CONSTRAINT [FK__dtl_ps__id_game__7F2BE32F]
GO
ALTER TABLE [dbo].[dtl_ps]  WITH CHECK ADD FOREIGN KEY([id_ps])
REFERENCES [dbo].[ms_ps] ([id_ps])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ms_meja]  WITH CHECK ADD  CONSTRAINT [FK__ms_meja__deskrip__49C3F6B7] FOREIGN KEY([id_ps])
REFERENCES [dbo].[ms_ps] ([id_ps])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[ms_meja] CHECK CONSTRAINT [FK__ms_meja__deskrip__49C3F6B7]
GO
ALTER TABLE [dbo].[trs_member]  WITH CHECK ADD  CONSTRAINT [FK__trs_membe__id_ka__6A30C649] FOREIGN KEY([id_karyawan])
REFERENCES [dbo].[ms_karyawan] ([id_karyawan])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[trs_member] CHECK CONSTRAINT [FK__trs_membe__id_ka__6A30C649]
GO
ALTER TABLE [dbo].[trs_member]  WITH CHECK ADD  CONSTRAINT [FK__trs_membe__id_me__693CA210] FOREIGN KEY([id_member])
REFERENCES [dbo].[ms_member] ([id_member])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[trs_member] CHECK CONSTRAINT [FK__trs_membe__id_me__693CA210]
GO
ALTER TABLE [dbo].[trs_paket]  WITH CHECK ADD  CONSTRAINT [FK_trs_paket_ms_karyawan] FOREIGN KEY([id_karyawan])
REFERENCES [dbo].[ms_karyawan] ([id_karyawan])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[trs_paket] CHECK CONSTRAINT [FK_trs_paket_ms_karyawan]
GO
ALTER TABLE [dbo].[trs_paket]  WITH CHECK ADD  CONSTRAINT [FK_trs_paket_ms_meja] FOREIGN KEY([id_meja])
REFERENCES [dbo].[ms_meja] ([id_meja])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[trs_paket] CHECK CONSTRAINT [FK_trs_paket_ms_meja]
GO
ALTER TABLE [dbo].[trs_paket]  WITH CHECK ADD  CONSTRAINT [FK_trs_paket_ms_member] FOREIGN KEY([id_member])
REFERENCES [dbo].[ms_member] ([id_member])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[trs_paket] CHECK CONSTRAINT [FK_trs_paket_ms_member]
GO
ALTER TABLE [dbo].[trs_peminjaman]  WITH CHECK ADD  CONSTRAINT [FK__trs_pemin__id_ka__534D60F1] FOREIGN KEY([id_karyawan])
REFERENCES [dbo].[ms_karyawan] ([id_karyawan])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[trs_peminjaman] CHECK CONSTRAINT [FK__trs_pemin__id_ka__534D60F1]
GO
ALTER TABLE [dbo].[trs_peminjaman]  WITH CHECK ADD  CONSTRAINT [FK__trs_pemin__id_me__52593CB8] FOREIGN KEY([id_member])
REFERENCES [dbo].[ms_member] ([id_member])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[trs_peminjaman] CHECK CONSTRAINT [FK__trs_pemin__id_me__52593CB8]
GO
ALTER TABLE [dbo].[trs_pengembalian]  WITH CHECK ADD  CONSTRAINT [FK_trs_pengembalian_trs_peminjaman] FOREIGN KEY([id_peminjaman])
REFERENCES [dbo].[trs_peminjaman] ([id_peminjaman])
GO
ALTER TABLE [dbo].[trs_pengembalian] CHECK CONSTRAINT [FK_trs_pengembalian_trs_peminjaman]
GO


CREATE VIEW	[dbo].[vw_game]
as
SELECT a.*, STRING_AGG(c.nama_kategori, ', ') AS kategori
FROM ms_game a
LEFT JOIN dtl_game b ON a.id_game = b.id_game
LEFT JOIN ms_kategorigame c ON c.id_kategori = b.id_kategori
GROUP BY a.id_game, a.nama_game, a.deskripsi_game, a.tanggal_rilis, a.jenis_ps
GO


CREATE VIEW [dbo].[transaksi_view] AS
SELECT
    p.id_peminjaman AS id_transaksi,
    p.tanggal_peminjaman AS tanggal_transaksi,
    p.biaya AS total_transaksi,
    'Borrowing' AS tipe_transaksi
FROM
    trs_peminjaman p
WHERE
    p.tanggal_peminjaman >= DATEADD(MONTH, -1, GETDATE())

UNION

SELECT
    pk.id_pembelian AS id_transaksi,
    CONVERT(DATE, pk.tanggal_pembelian, 103) AS tanggal_transaksi,
    pk.jumlah_bayar AS total_transaksi,
    'Services' AS tipe_transaksi
FROM
    trs_paket pk
WHERE
    CONVERT(DATE, pk.tanggal_pembelian, 103) >= DATEADD(MONTH, -1, GETDATE())

UNION

SELECT
    pe.id_pengembalian AS id_transaksi,
    pe.tanggal_pengembalian AS tanggal_transaksi,
    pe.denda AS total_transaksi,
    'Returning Fine' AS tipe_transaksi
FROM
    trs_pengembalian pe
WHERE
    pe.denda <> 0
    AND pe.tanggal_pengembalian >= DATEADD(MONTH, -1, GETDATE())

UNION

SELECT
    tm.id_trsmember AS id_transaksi,
    tm.tanggal_mulai AS tanggal_transaksi,
    tm.total_biaya AS total_transaksi,
    'Member' AS tipe_transaksi
FROM
    trs_member tm
WHERE
    tm.tanggal_mulai >= DATEADD(MONTH, -1, GETDATE());
GO
/****** Object:  View [dbo].[vw_subscription_renewal]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[vw_subscription_renewal] AS
SELECT
    id_member AS subscription,
    0 AS renewal,
    DATEPART(MONTH, tanggal_mulai) AS [month]
FROM
    [dbo].[trs_member]
GROUP BY
    id_member,
    DATEPART(MONTH, tanggal_mulai);
GO
ALTER VIEW [dbo].[vw_subscription_renewal] AS
SELECT
    id_member AS subscription,
    0 AS renewal,
    DATEPART(MONTH, tanggal_mulai) AS [month]
FROM
    [dbo].[trs_member]
GROUP BY
    id_member,
    DATEPART(MONTH, tanggal_mulai)

UNION ALL

SELECT
    id_member AS subscription,
    COUNT(*) AS renewal,
    DATEPART(MONTH, tanggal_mulai) AS [month]
FROM
    [dbo].[trs_member]
WHERE
    id_member NOT IN (
        SELECT id_member FROM [dbo].[vw_subscription_renewal] WHERE renewal = 0
    )
GROUP BY
    id_member,
    DATEPART(MONTH, tanggal_mulai);
GO


/****** Object:  View [dbo].[total_all]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [dbo].[total_all] AS
SELECT
    p.id_peminjaman AS id_transaksi,
    p.tanggal_peminjaman AS tanggal_transaksi,
    p.biaya AS total_transaksi,
    'Borrowing' AS tipe_transaksi
FROM
    trs_peminjaman p
UNION

SELECT
    pk.id_pembelian AS id_transaksi,
    CONVERT(DATE, CONVERT(DATETIME, pk.tanggal_pembelian, 120)) AS tanggal_transaksi,
    pk.jumlah_bayar AS total_transaksi,
    'Services' AS tipe_transaksi
FROM
    trs_paket pk

UNION

SELECT
    pe.id_pengembalian AS id_transaksi,
    pe.tanggal_pengembalian AS tanggal_transaksi,
    pe.denda AS total_transaksi,
    'Returning Fine' AS tipe_transaksi
FROM
    trs_pengembalian pe
WHERE
    pe.denda <> 0
UNION

SELECT
    tm.id_trsmember AS id_transaksi,
    tm.tanggal_mulai AS tanggal_transaksi,
    tm.total_biaya AS total_transaksi,
    'Member' AS tipe_transaksi
FROM
    trs_member tm
GO
/****** Object:  View [dbo].[current_play]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[current_play] AS
SELECT
    id_pembelian,
    CONVERT(DATETIME, tanggal_pembelian, 120) AS tanggal_pembelian,
    id_karyawan,
    nama_pembeli,
    id_meja,
    total_transaksi,
    jumlah_bayar,
    CONVERT(DATETIME, tanggal_selesai, 120) AS tanggal_selesai
FROM
    trs_paket
WHERE
    CONVERT(DATETIME, tanggal_selesai, 120) > GETDATE()

GO
/****** Object:  Table [dbo].[ms_ps]    Script Date: 2024-04-17 08:48:15 ******/

/****** Object:  View [dbo].[asset]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [dbo].[asset] AS
SELECT
    [id_aksesoris] AS [id_asset],
    [nama_aksesoris] AS [nama_asset],
    [harga_sewa] AS [harga_asset],
    [jenis_aksesoris] AS [jenis_asset],
    [kondisi_aksesoris] AS [kondisi_asset],
    'Aksesoris' AS [tipe_asset]
FROM
    [dbo].[ms_aksesoris]

UNION ALL

SELECT
    [id_ps] AS [id_asset],
    [no_seri] AS [nama_asset],
    [harga_sewa] AS [harga_asset],
    [jenis_ps] AS [jenis_asset],
    [kondisi_ps] AS [kondisi_asset],
    'PS' AS [tipe_asset]
FROM
    [dbo].[ms_ps]
GO
/****** Object:  Table [dbo].[dtl_pinjamaksesoris]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

/****** Object:  View [dbo].[YourViewName]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[YourViewName] AS
SELECT 
    trs_pengembalian.id_pengembalian,
    trs_pengembalian.tanggal_pengembalian,
    trs_pengembalian.denda,
    trs_pengembalian.id_karyawan,
    trs_pengembalian.bayar,
    dtl_pinjamaksesoris.id_peminjaman,
    dtl_pinjamaksesoris.id_aksesoris,
    dtl_pinjamaksesoris.kondisi_pengembalian
FROM 
    trs_pengembalian
JOIN 
    dtl_pinjamaksesoris ON trs_pengembalian.id_peminjaman = dtl_pinjamaksesoris.id_peminjaman;
GO
/****** Object:  Table [dbo].[dtl_peminjamanps]    Script Date: 2024-04-17 08:48:15 ******/

/****** Object:  View [dbo].[asset_return]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE VIEW [dbo].[asset_return] AS
SELECT
	da.id_peminjaman,
    da.id_aksesoris AS id_asset,
    p.id_member,
    p.tanggal_pengembalian
FROM
    trs_peminjaman p
JOIN
    dtl_pinjamaksesoris da ON p.id_peminjaman = da.id_peminjaman

UNION

SELECT
	dp.id_peminjaman,
    dp.id_ps AS id_asset,
    p.id_member,
    p.tanggal_pengembalian
FROM
    trs_peminjaman p

JOIN
    dtl_peminjamanps dp ON p.id_peminjaman = dp.id_peminjaman;
GO
/****** Object:  Table [dbo].[bntu_member]    Script Date: 2024-04-17 08:48:15 ******/

/****** Object:  Index [ms_karyawan_nama_karyawan]    Script Date: 2024-04-17 08:48:15 ******/
CREATE NONCLUSTERED INDEX [ms_karyawan_nama_karyawan] ON [dbo].[ms_karyawan]
(
	[nama_karyawan] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [nci_filterfortrs_member]    Script Date: 2024-04-17 08:48:15 ******/

/****** Object:  StoredProcedure [dbo].[sp_DeleteAndInsertLogin]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_DeleteAndInsertLogin]
    @id varchar(10)
AS
BEGIN
    -- Delete all data from login table
    DELETE FROM login;

    -- Insert id_karyawan and foto_profil from ms_karyawan
    INSERT INTO login (id_karyawan, foto_profil, nama_karyawan)
    SELECT id_karyawan, foto_profil, nama_karyawan
    FROM ms_karyawan
    WHERE id_karyawan = @id;
END;
GO

CREATE FUNCTION [dbo].[fn_count_active_members]()
RETURNS INT
AS
BEGIN
    DECLARE @count_active_members INT;

    SELECT @count_active_members = COUNT(*)
    FROM ms_member
    WHERE status_anggota = 'Active';

    RETURN @count_active_members;
END;
GO
/****** Object:  UserDefinedFunction [dbo].[fn_count_karyawan]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[fn_count_karyawan]()
RETURNS INT
AS
BEGIN
    DECLARE @count_karyawan INT;

    SELECT @count_karyawan = COUNT(*)
    FROM ms_karyawan
	WHERE id_karyawan <> 'OWNER'

    RETURN @count_karyawan;
END;
GO
/****** Object:  UserDefinedFunction [dbo].[fn_income_this_month]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[fn_income_this_month]()
RETURNS MONEY
AS
BEGIN
    DECLARE @income_this_month MONEY;

    SELECT @income_this_month = SUM(CASE WHEN MONTH(tanggal_transaksi) = MONTH(GETDATE()) THEN total_transaksi ELSE 0 END)
    FROM total_all;

    RETURN @income_this_month;
END;
GO
/****** Object:  UserDefinedFunction [dbo].[fn_income_today]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[fn_income_today]()
RETURNS MONEY
AS
BEGIN
    DECLARE @income_today MONEY;

    SELECT @income_today = SUM(total_transaksi)
    FROM total_all
    WHERE tanggal_transaksi = CONVERT(DATE, GETDATE());

    RETURN @income_today;
END;
GO
/****** Object:  UserDefinedFunction [dbo].[fn_total_income]    Script Date: 2024-04-17 08:48:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- Membuat fungsi untuk menghitung total pendapatan
CREATE FUNCTION [dbo].[fn_total_income]()
RETURNS MONEY
AS
BEGIN
    DECLARE @total_income MONEY;

    SELECT @total_income = SUM(total_transaksi)
    FROM total_all;

    RETURN @total_income;
END;
GO

INSERT INTO [dbo].[ms_karyawan]
           ([id_karyawan]
           ,[nama_karyawan]
           ,[alamat]
           ,[no_telp]
           ,[foto_profil]
           ,[nama_pengguna]
           ,[kata_sandi]
           ,[isDeleted])
     VALUES
           ('KRY001'
           ,'Default Admin'
           ,'Default Admin'
           ,'Default Admin'
           ,NULL
           ,'admin'
           ,'admin'
           ,NULL)
	GO


