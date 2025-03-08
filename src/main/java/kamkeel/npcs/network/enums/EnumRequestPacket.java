package kamkeel.npcs.network.enums;

public enum EnumRequestPacket {
    NPCDelete,
    RemoteMainMenu,
    RemoteGlobalMenu,
    NpcClose,
    RemoteDelete,
    RemoteFreeze,
    RemoteFreezeGet,
    RemoteReset,
    SpawnMob,
    MobSpawner,

    DimensionsGet,

    MainmenuAISave,
    MainmenuAIGet,
    MainmenuInvSave,
    MainmenuInvGet,
    MainmenuStatsSave,
    MainmenuStatsGet,
    MainmenuDisplaySave,
    MainmenuDisplayGet,
    ModelDataSave,
    MainmenuAdvancedSave,
    MainmenuAdvancedGet,
    MainmenuAdvancedMarkData,

    DialogNpcSet,
    DialogNpcRemove,
    DialogNpcGet,
    DialogCategorySave,
    DialogCategoriesGet,
    DialogCategoryRemove,
    DialogCategoryGet,
    DialogSave,
    DialogsGet,
    DialogGet,
    DialogRemove,

    TransportSave,
    TransportCategoriesGet,
    TransportCategorySave,
    TransportCategoryRemove,
    TransportRemove,
    TransportGetLocation,
    TransportsGet,

    FactionSet,
    FactionSave,
    FactionsGet,
    FactionGet,
    FactionRemove,

    TagSet,
    TagSave,
    TagsGet,
    TagGet,
    NpcTagsGet,
    TagRemove,

    QuestUntrack,
    QuestLogToServer,
    QuestCategorySave,
    QuestCategoriesGet,
    QuestRemove,
    QuestCategoryRemove,
    QuestRewardSave,
    QuestSave,
    QuestsGetFromQuest,
    QuestsGet,
    QuestDialogGetTitle,
    QuestOpenGui,
    QuestGet,

    RecipeSave,
    RecipeRemove,
    RecipesGet,
    RecipeGet,

    NaturalSpawnSave,
    NaturalSpawnGet,
    NaturalSpawnRemove,
    NaturalSpawnGetAll,

    MerchantUpdate,
    MountPacket,
    MovingPathSave,
    MovingPathGet,

    AnimationsGet,
    AnimationGet,
    AnimationRemove,
    AnimationSave,

    RemoteNpcsGet,
    RemoteTpToNpc,
    TileEntitySave,
    TileEntityGet,
    Gui,
    IsGuiOpen,
    CacheAnimation,

    PartyLogToServer,
    PartySave,
    PartyInfo,
    PartyDisband,
    PartyKick,
    PartyLeave,
    PartyInvite,
    PartyAcceptInvite,
    PartyIgnoreInvite,
    PartySetLeader,
    PartySetQuest,

    LinkedNPCRemove,
    LinkedNPCAdd,
    LinkedGetAll,
    LinkedSet,
    LinkedGet,
    LinkedItemSave,
    LinkedItemRemove,
    LinkedItemBuild,
    LinkedItemScript,

    BankGet,
    BanksGet,
    BankSave,
    BankRemove,

    QuestCategoryGet,

    PlayerDataInfo,
    PlayerDataGetNames,
    PlayerDataDeleteInfo,
    PlayerDataRemove,
    PlayerDataMapRegen,
    PlayerDataSave,

    JobSave,
    JobGet,
    JobSpawnerAdd,
    JobSpawnerRemove,

    RoleCompanionUpdate,
    RoleSave,
    RoleGet,

    MailOpenSetup,

    TransformSave,
    TransformGet,
    TransformLoad,

    TraderMarketSave,

    CloneList,
    CloneSave,
    CloneRemove,
    CloneTagList,
    CloneAllTags,
    CloneAllTagsShort,
    ClonePreSave,

    EventScript,
    PlayerScript,
    ForgeScript,
    GlobalNPCScript,
    ItemScript,
    ItemScriptError,
    BlockScript,
    NPCScript,
    ScriptInfo,
    EffectScript,

    DimensionTeleport,

    EffectList,
    EffectGet,
    EffectScriptGet,
    EffectRemove,
    EffectSave,

    ColorSet,
    ColorBrush,

    ProfileGet,
    ProfileGetAll,
    ProfileGetInfo,
    ProfileCreate,
    ProfileRemove,
    ProfileRename,
    ProfileChange,

    MagicCycleRemove,
    MagicCycleSave,
    MagicGetAll,
    MagicRemove,
    MagicGet,
    MagicSave,
    NpcMagicGet,
    NpcMagicSave,
}
